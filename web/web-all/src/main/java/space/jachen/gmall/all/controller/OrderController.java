package space.jachen.gmall.all.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.order.OrderFeignClient;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/3/20 0:00
 */
@Controller
@SuppressWarnings("all")
public class OrderController {

    @Autowired
    private OrderFeignClient orderFeignClient;

    /**
     * 确认订单
     * @param model
     * @return
     */
    @GetMapping("trade.html")
    public String trade(Model model) {
        Result<Map<String, Object>> result = orderFeignClient.trade();

        model.addAllAttributes(result.getData());
        return "order/trade";
    }
}
