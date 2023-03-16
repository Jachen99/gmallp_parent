package space.jachen.gmall.list.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.domain.list.SearchParam;
import space.jachen.gmall.domain.list.SearchResponseVo;

/**
 * @author JaChen
 * @date 2023/3/14 19:43
 */
@FeignClient(value = "service-list",fallback = ListDegradeFeignClient.class)
public interface ListFeignClient {

    public static final String BaseURL = "/api/list";


    /**
     * 商品搜索
     * @param searchParam  SearchParam
     * @return  SearchResponseVo
     */
    @PostMapping(BaseURL)
    public Result<SearchResponseVo> list(@RequestBody SearchParam searchParam);



    /**
     * 更新商品的热度排名
     * @param skuId  skuId
     * @return Result
     */
    @GetMapping(BaseURL+"/inner/incrHotScore/{skuId}")
    public Result<String> incrHotScore(@PathVariable Long skuId);

}
