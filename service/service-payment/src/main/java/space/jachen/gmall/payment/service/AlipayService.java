package space.jachen.gmall.payment.service;

/**
 * @author JaChen
 * @date 2023/3/22 19:15
 */
public interface AlipayService {

    /**
     * 创建支付
     * @param orderId
     * @return
     */
    String createdPay(Long orderId);
}
