package space.jachen.gmall.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.domain.order.OrderInfo;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/3/19 23:57
 */
@FeignClient(value = "service-order", fallback = OrderDegradeFeignClient.class)
public interface OrderFeignClient {

    public static final String BaseURL = "/api/order";

    /**
     * 内部调用获取订单
     * @param orderId
     * @return
     */
    @GetMapping("inner/getOrderInfo/{orderId}")
    public Result<OrderInfo> getOrderInfo(@PathVariable Long orderId);

    /**
     * 确认订单
     * @return
     */
    @GetMapping(BaseURL+"/auth/trade")
    Result<Map<String, Object>> trade();
}
