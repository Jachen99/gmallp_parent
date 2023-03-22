package space.jachen.gmall.payment.service;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/3/22 19:15
 */
public interface AlipayService {

    /**
     * 退款接口
     * @param orderId
     * @return
     */
    boolean refund(Long orderId);

    /**
     * 创建支付
     * @param orderId
     * @return
     */
    String createdPay(Long orderId);

    /**
     * 支付宝异步回调
     *      - 需要内网穿透
     * @param paramsMap  Map<String, String>
     * @return  String
     */
    String callbackNotify(Map<String, String> paramsMap);
}
