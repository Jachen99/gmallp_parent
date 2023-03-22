package space.jachen.gmall.payment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.payment.config.AlipayConfig;
import space.jachen.gmall.payment.service.AlipayService;

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