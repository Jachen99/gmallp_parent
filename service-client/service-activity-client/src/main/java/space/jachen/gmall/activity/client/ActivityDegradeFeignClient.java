package space.jachen.gmall.activity.client;

import org.springframework.stereotype.Component;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.domain.activity.SeckillGoods;

import java.util.List;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/3/24 20:10
 */
@Component
public class ActivityDegradeFeignClient implements ActivityFeignClient {
    @Override
    public Result<Map<String, Object>> trade() {
        return null;
    }

    @Override
    public Result<List<SeckillGoods>> findAll() {
        return null;
    }

    @Override
    public Result<SeckillGoods> getSeckillGoods(Long skuId) {
        return null;
    }
}
