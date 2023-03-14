package space.jachen.gmall.list.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import space.jachen.gmall.common.result.Result;

/**
 * @author JaChen
 * @date 2023/3/14 19:43
 */
@FeignClient(value = "service-list",fallback = ListDegradeFeignClient.class)
public interface ListFeignClient {

    public static final String BaseURL = "/api/list";

    /**
     * 更新商品的热度排名
     * @param skuId  skuId
     * @return Result
     */
    @GetMapping(BaseURL+"/inner/incrHotScore/{skuId}")
    public Result<String> incrHotScore(@PathVariable Long skuId);

}
