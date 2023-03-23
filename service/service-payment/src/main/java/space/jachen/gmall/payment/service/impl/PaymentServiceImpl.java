package space.jachen.gmall.payment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import space.jachen.gmall.common.constant.MqConst;
import space.jachen.gmall.common.service.RabbitService;
import space.jachen.gmall.domain.enums.PaymentStatus;
import space.jachen.gmall.domain.order.OrderInfo;
import space.jachen.gmall.domain.payment.PaymentInfo;
import space.jachen.gmall.payment.mapper.PaymentInfoMapper;
import space.jachen.gmall.payment.service.PaymentService;

import java.util.Date;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/3/22 18:44
 */
@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentInfoMapper,PaymentInfo> implements PaymentService {
    @Autowired
    private PaymentInfoMapper paymentInfoMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RabbitService rabbitService;
    @Override
    public void savePaymentInfo(OrderInfo orderInfo, String paymentType) {

        QueryWrapper<PaymentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderInfo.getId());
        queryWrapper.eq("payment_type", paymentType);
        Integer count = paymentInfoMapper.selectCount(queryWrapper);
        if(count > 0) return;
        // 保存交易记录
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setUserId(orderInfo.getUserId());
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setOrderId(orderInfo.getId());
        paymentInfo.setPaymentType(paymentType);
        paymentInfo.setOutTradeNo(orderInfo.getOutTradeNo());
        paymentInfo.setPaymentStatus(PaymentStatus.UNPAID.name());
        paymentInfo.setSubject(orderInfo.getTradeBody());
        paymentInfo.setTotalAmount(orderInfo.getTotalAmount());

        paymentInfoMapper.insert(paymentInfo);
    }

    @Override
    public PaymentInfo getPaymentInfo(String outTradeNo, String name) {
        QueryWrapper<PaymentInfo> paymentInfoQueryWrapper = new QueryWrapper<>();
        paymentInfoQueryWrapper.eq("out_trade_no",outTradeNo);
        paymentInfoQueryWrapper.eq("payment_type",name);

        return paymentInfoMapper.selectOne(paymentInfoQueryWrapper);
    }

    @Override
    public void paySuccess(String outTradeNo, String name, Map<String, String> paramsMap) {
        //  根据outTradeNo，paymentType 查询
        PaymentInfo paymentInfoQuery = this.getPaymentInfo(outTradeNo, name);
        if (paymentInfoQuery==null){
            return;
        }
        try {
            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setCallbackTime(new Date());
            paymentInfo.setPaymentStatus(PaymentStatus.PAID.name());
            paymentInfo.setCallbackContent(paramsMap.toString());
            paymentInfo.setTradeNo(paramsMap.get("trade_no"));
            // 更新
            QueryWrapper<PaymentInfo> paymentInfoQueryWrapper = new QueryWrapper<>();
            paymentInfoQueryWrapper.eq("out_trade_no",outTradeNo);
            paymentInfoQueryWrapper.eq("payment_type",name);
            paymentInfoMapper.update(paymentInfo,paymentInfoQueryWrapper);
            // 发送消息更新订单状态
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_PAYMENT_PAY,MqConst.ROUTING_PAYMENT_PAY,paymentInfoQuery.getOrderId());
        } catch (Exception e) {
            //  删除key
            this.stringRedisTemplate.delete(paramsMap.get("notify_id"));
            e.printStackTrace();
        }
    }

    @Override
    public void closePayment(Long orderId) {
        // 设置关闭交易记录的条件  118
        QueryWrapper<PaymentInfo> paymentInfoQueryWrapper = new QueryWrapper<>();
        paymentInfoQueryWrapper.eq("order_id",orderId);
        // 如果当前的交易记录不存在，则不更新交易记录
        Integer count = paymentInfoMapper.selectCount(paymentInfoQueryWrapper);
        if (null == count || count.intValue()==0) return;
        // 在关闭支付宝交易之前。还需要关闭paymentInfo
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setPaymentStatus(PaymentStatus.CLOSED.name());
        paymentInfoMapper.update(paymentInfo,paymentInfoQueryWrapper);
    }

}
