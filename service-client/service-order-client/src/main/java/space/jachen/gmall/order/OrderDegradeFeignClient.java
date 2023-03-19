package space.jachen.gmall.order;

import org.springframework.stereotype.Component;
import space.jachen.gmall.common.result.Result;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/3/19 23:57
 */
@Component
public class OrderDegradeFeignClient implements OrderFeignClient {

    @Override
    public Result<Map<String, Object>> trade() {
        return Result.fail();
    }
}
