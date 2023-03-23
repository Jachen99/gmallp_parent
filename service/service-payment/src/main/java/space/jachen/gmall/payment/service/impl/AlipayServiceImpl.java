package space.jachen.gmall.payment.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import space.jachen.gmall.domain.enums.PaymentStatus;
import space.jachen.gmall.domain.enums.PaymentType;
import space.jachen.gmall.domain.order.OrderInfo;
import space.jachen.gmall.domain.payment.PaymentInfo;
import space.jachen.gmall.order.OrderFeignClient;
import space.jachen.gmall.payment.config.AlipayConfig;
import space.jachen.gmall.payment.service.AlipayService;
import space.jachen.gmall.payment.service.PaymentService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author JaChen
 * @date 2023/3/22 19:15
 */
@Service
@SuppressWarnings("all")
@Slf4j
public class AlipayServiceImpl implements AlipayService {
    @Autowired
    private OrderFeignClient orderFeignClient;
    @Autowired
    private AlipayClient alipayClient;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @SneakyThrows
    @Override
    public Boolean closePay(Long orderId) {
        OrderInfo orderInfo = orderFeignClient.getOrderInfo(orderId).getData();
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        HashMap<String, Object> map = new HashMap<>();
        // map.put("trade_no",paymentInfo.getTradeNo()); // 从paymentInfo 中获取
        map.put("out_trade_no",orderInfo.getOutTradeNo());
        // map.put("operator_id","YX01");
        request.setBizContent(JSON.toJSONString(map));
        AlipayTradeCloseResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
            return true;
        } else {
            System.out.println("调用失败");
            return false;
        }
    }


    @Override
    public boolean refund(Long orderId) {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        OrderInfo orderInfo = orderFeignClient.getOrderInfo(orderId).getData();
        HashMap<String, Object> map = new HashMap<>();
        map.put("out_trade_no", orderInfo.getOutTradeNo());
        map.put("refund_amount", 0.01);
        map.put("refund_reason", "不想要了");
        request.setBizContent(JSON.toJSONString(map));
        AlipayTradeRefundResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if (response.isSuccess()) {
            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setPaymentStatus(PaymentStatus.CLOSED.name());
            LambdaQueryWrapper<PaymentInfo> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(PaymentInfo::getOutTradeNo,orderInfo.getOutTradeNo());
            paymentService.update(paymentInfo,queryWrapper);
            return true;
        } else {
            return false;
        }

    }

    @Override
    @SneakyThrows
    public String createdPay(Long orderId) {
        // 获取订单信息
        OrderInfo orderInfo = orderFeignClient.getOrderInfo(orderId).getData();
        if (orderInfo == null)
            return "订单异常，支付失败，请联系管理员...";
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        // 异步接收地址，仅支持http/https，公网可访问
        request.setNotifyUrl(AlipayConfig.notify_payment_url);
        // 同步跳转地址，仅支持http/https
        request.setReturnUrl(AlipayConfig.return_payment_url);
        /******必传参数******/
        JSONObject bizContent = new JSONObject();
        // 商户订单号，商家自定义，保持唯一性
        bizContent.put("out_trade_no", orderInfo.getOutTradeNo());
        // 支付金额，最小值0.01元
        bizContent.put("total_amount", 0.01);
        // 订单标题，不可使用特殊符号
        bizContent.put("subject", orderInfo.getTradeBody());
        // 电脑网站支付场景固定传值FAST_INSTANT_TRADE_PAY
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());
        AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
        if (response.isSuccess()) {
            log.info("调用成功");
            paymentService.savePaymentInfo(orderInfo,PaymentType.ALIPAY.name());
            return response.getBody();
        }
        log.error("调用失败");
        return "支付失败，请联系管理员...";
    }

    @Override
    public String callbackNotify(Map<String, String> paramsMap) {

        // 对支付宝异步回调方法进行验签
        log.info("支付异步回调验签方法执行.....");
        boolean flag = false;
        try {
            // 验签
            flag = AlipaySignature.rsaCheckV1(paramsMap, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);
        } catch (AlipayApiException e) {
            flag = false;
            e.printStackTrace();
        }
        //  获取异步通知的参数中的订单号
        String outTradeNo = paramsMap.get("out_trade_no");
        //  获取异步通知的参数中的订单总金额
        String totalAmount = paramsMap.get("total_amount");
        //  获取异步通知的参数中的appId
        String appId = paramsMap.get("app_id");
        //  获取异步通知的参数中的交易状态
        String tradeStatus = paramsMap.get("trade_status");
        //  保证异步通知的幂等性 notify_id
        String notifyId = paramsMap.get("notify_id");
        //  根据outTradeNo 查询数据
        PaymentInfo paymentinfo = this.paymentService.getPaymentInfo(outTradeNo, PaymentType.ALIPAY.name());
        // 验签成功需要进行二次校验
        if (flag) {
            if (paymentinfo == null || new BigDecimal("0.01").compareTo(new BigDecimal(totalAmount)) != 0
                    || !appId.equals(AlipayConfig.app_id)) {

                return "failure";
            }
            //  setnx：当 key 不存在的时候生效
            Boolean redisFlag = this.stringRedisTemplate.opsForValue().setIfAbsent(notifyId, notifyId, 1462, TimeUnit.MINUTES);
            //  说明已经处理过了
            if (!redisFlag) {

                return "failure";
            }
            if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                //  修改交易记录状态 再订单状态
                this.paymentService.paySuccess(outTradeNo, PaymentType.ALIPAY.name(), paramsMap);

                return "success";
            }

            return "failure";
        }

        return "failure";
    }
}
