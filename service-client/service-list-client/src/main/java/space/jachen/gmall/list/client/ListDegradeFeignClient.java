package space.jachen.gmall.list.client;

import space.jachen.gmall.common.result.Result;

/**
 * @author JaChen
 * @date 2023/3/14 19:45
 */
public class ListDegradeFeignClient implements ListFeignClient{
    @Override
    public Result<String> incrHotScore(Long skuId) {
        return null;
    }
}
