package space.jachen.gmall.list.client;

import org.springframework.stereotype.Component;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.domain.list.SearchParam;
import space.jachen.gmall.domain.list.SearchResponseVo;

/**
 * @author JaChen
 * @date 2023/3/14 19:45
 */
@Component
public class ListDegradeFeignClient implements ListFeignClient{
    @Override
    public Result<SearchResponseVo> list(SearchParam searchParam) {
        return null;
    }

    @Override
    public Result<String> incrHotScore(Long skuId) {
        return null;
    }
}
