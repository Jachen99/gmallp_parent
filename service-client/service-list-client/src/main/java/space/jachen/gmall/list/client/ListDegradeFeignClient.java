package space.jachen.gmall.list.client;

import org.springframework.stereotype.Component;
import space.jachen.gmall.common.result.Result;

/**
 * @author JaChen
 * @date 2023/3/14 19:45
 */
@Component
public class ListDegradeFeignClient implements ListFeignClient{
    @Override
    public Result<String> incrHotScore(Long skuId) {
        return null;
    }
}
