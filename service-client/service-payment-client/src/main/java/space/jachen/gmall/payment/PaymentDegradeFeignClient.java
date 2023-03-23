package space.jachen.gmall.payment;

import org.springframework.stereotype.Component;
import space.jachen.gmall.domain.payment.PaymentInfo;

/**
 * @author JaChen
 * @date 2023/3/24 0:22
 */
@Component
public class PaymentDegradeFeignClient implements PaymentFeignClient {
    @Override
    public PaymentInfo getPaymentInfo(String outTradeNo) {
        return null;
    }

    @Override
    public Boolean checkPayment(Long orderId) {
        return null;
    }

    @Override
    public Boolean closePay(Long orderId) {
        return null;
    }
}
