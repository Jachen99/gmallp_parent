package space.jachen.gmall.all.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import space.jachen.gmall.domain.order.OrderInfo;
import space.jachen.gmall.order.OrderFeignClient;

import javax.servlet.http.HttpServletRequest;

/**
 * @author JaChen
 * @date 2023/3/22 18:18
 */
@Controller
@SuppressWarnings("all")
public class PaymentController {

    @Autowired
    private OrderFeignClient orderFeignClient;

    /**
     * 支付成功页
     * @return
     */
    @GetMapping("pay/success.html")
    public String success() {

        return "payment/success";
    }


    /**
     * 支付页
     * @param request
     * @return
     */
    @GetMapping("pay.html")
    public String success(HttpServletRequest request, Model model) {
        String orderId = request.getParameter("orderId");
        OrderInfo orderInfo = orderFeignClient.getOrderInfo(Long.parseLong(orderId)).getData();
        model.addAttribute("orderInfo", orderInfo);

        return "payment/pay";
    }
}
