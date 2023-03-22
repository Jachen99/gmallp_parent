package space.jachen.gmall.payment.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.jachen.gmall.domain.order.OrderInfo;
import space.jachen.gmall.order.OrderFeignClient;
import space.jachen.gmall.payment.config.AlipayConfig;
import space.jachen.gmall.payment.service.AlipayService;
import space.jachen.gmall.payment.service.PaymentService;

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
        if(response.isSuccess()){
            log.info("调用成功");
            return response.getBody();
        }
        log.error("调用失败");
        return "支付失败，请联系管理员...";
    }
}
