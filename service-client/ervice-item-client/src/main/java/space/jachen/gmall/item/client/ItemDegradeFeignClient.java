package space.jachen.gmall.item.client;

import org.springframework.stereotype.Component;
import space.jachen.gmall.common.result.Result;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/3/9 15:34
 */
@Component
public class ItemDegradeFeignClient implements ItemFeignClient{
    @Override
    public Result<Map<String, Object>> getSkuId(Long skuId) {
        return null;
    }
}
