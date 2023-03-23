package space.jachen.gmall.payment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.domain.enums.PaymentType;
import space.jachen.gmall.domain.payment.PaymentInfo;
import space.jachen.gmall.payment.config.AlipayConfig;
import space.jachen.gmall.payment.service.AlipayService;
import space.jachen.gmall.payment.service.PaymentService;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/3/22 19:16
 */
@Controller
@RequestMapping("/api/payment/alipay")
@Slf4j
public class AlipayController {

    @Autowired
    private AlipayService alipayService;
    @Autowired
    private PaymentService paymentService;


    /**
     * 根据outTradeNo、ALIPAY查询订单信息
     * @param outTradeNo
     * @return
     */
    @GetMapping("getPaymentInfo/{outTradeNo}")
    @ResponseBody
    public PaymentInfo getPaymentInfo(@PathVariable String outTradeNo){
        return paymentService.getPaymentInfo(outTradeNo, PaymentType.ALIPAY.name());
    }



    /**
     * 查看是否有交易记录
     * @param orderId
     * @return
     */
    @RequestMapping("checkPayment/{orderId}")
    @ResponseBody
    public Boolean checkPayment(@PathVariable Long orderId){
        // 调用退款接口
        return alipayService.checkPayment(orderId);
    }



    /**
     * 根据订单Id关闭订单
     * @param orderId
     * @return
     */
    @GetMapping("closePay/{orderId}")
    @ResponseBody
    public Boolean closePay(@PathVariable Long orderId){
        return alipayService.closePay(orderId);
    }



    /**
     * 退款
     * @param orderId
     * @return
     */
    @RequestMapping("refund/{orderId}")
    @ResponseBody
    public Result<Boolean> refund(@PathVariable(value = "orderId")Long orderId) {
        boolean flag = alipayService.refund(orderId);

        return Result.ok(flag);
    }



    /**
     * 支付宝异步回调
     *      - 需要内网穿透
     * @param paramsMap  Map<String, String>
     * @return  String
     */
    @PostMapping("/callback/notify")
    @ResponseBody
    public String callbackNotify(@RequestParam Map<String, String> paramsMap){

        return alipayService.callbackNotify(paramsMap);
    }


    /**
     * 支付宝同步回调
     * @return  String
     */
    @RequestMapping("callback/return")
    public String callBack() {
        // 同步回调给用户展示信息
        return "redirect:" + AlipayConfig.return_order_url;
    }

    /**
     * 创建支付请求
     * @param orderId  orderId
     * @return  String
     */
    @RequestMapping("submit/{orderId}")
    @ResponseBody
    public String submitOrder(@PathVariable Long orderId) {
        return alipayService.createdPay(orderId);
    }

}