package space.jachen.gmall.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import space.jachen.gmall.common.result.Result;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/3/19 23:57
 */
@FeignClient(value = "service-order", fallback = OrderDegradeFeignClient.class)
public interface OrderFeignClient {

    public static final String BaseURL = "/api/order";
    @GetMapping(BaseURL+"/auth/trade")
    Result<Map<String, Object>> trade();
}
