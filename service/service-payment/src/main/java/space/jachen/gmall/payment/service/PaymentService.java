package space.jachen.gmall.payment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import space.jachen.gmall.domain.order.OrderInfo;
import space.jachen.gmall.domain.payment.PaymentInfo;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/3/22 18:43
 */
public interface PaymentService extends IService<PaymentInfo> {

    /**
     * 保存交易记录
     * @param orderInfo
     * @param paymentType 支付类型（1：微信 2：支付宝）
     */
    void savePaymentInfo(OrderInfo orderInfo, String paymentType);

    /**
     * 查询交易记录  - 验签
     * @param outTradeNo   outTradeNo
     * @param name  name
     * @return  PaymentInfo
     */
    PaymentInfo getPaymentInfo(String outTradeNo, String name);

    /**
     * 校验成功修改订单状态
     * @param outTradeNo  outTradeNo
     * @param name  name
     * @param paramsMap  Map<String, String>
     */
    void paySuccess(String outTradeNo, String name, Map<String, String> paramsMap);

    /**
     * 关闭订单交易
     */
    void closePayment(Long orderId);
}
