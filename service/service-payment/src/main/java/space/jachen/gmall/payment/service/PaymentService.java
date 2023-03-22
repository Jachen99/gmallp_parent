package space.jachen.gmall.payment.service;

import space.jachen.gmall.domain.order.OrderInfo;

/**
 * @author JaChen
 * @date 2023/3/22 18:43
 */
public interface PaymentService {

    /**
     * 保存交易记录
     * @param orderInfo
     * @param paymentType 支付类型（1：微信 2：支付宝）
     */
    void savePaymentInfo(OrderInfo orderInfo, String paymentType);

}
