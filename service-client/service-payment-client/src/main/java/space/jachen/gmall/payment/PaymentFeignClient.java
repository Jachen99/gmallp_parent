package space.jachen.gmall.payment;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import space.jachen.gmall.domain.payment.PaymentInfo;

/**
 * @author JaChen
 * @date 2023/3/24 0:22
 */
@FeignClient(value = "service-payment",fallback = PaymentDegradeFeignClient.class)
public interface PaymentFeignClient{


    public static final String BaseURL = "/api/payment/alipay";


    /**
     * 根据outTradeNo、ALIPAY查询订单信息
     * @param outTradeNo
     * @return
     */
    @GetMapping("/getPaymentInfo/{outTradeNo}")
    @ResponseBody
    public PaymentInfo getPaymentInfo(@PathVariable String outTradeNo);



    /**
     * 查看是否有交易记录
     * @param orderId
     * @return
     */
    @RequestMapping("/checkPayment/{orderId}")
    @ResponseBody
    public Boolean checkPayment(@PathVariable Long orderId);



    /**
     * 根据订单Id关闭订单
     * @param orderId
     * @return
     */
    @GetMapping("/closePay/{orderId}")
    @ResponseBody
    public Boolean closePay(@PathVariable Long orderId);


}
