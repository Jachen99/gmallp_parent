package space.jachen.gmall.item.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import space.jachen.gmall.common.result.Result;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/3/9 15:34
 */
@FeignClient(value ="service-item", fallback = ItemDegradeFeignClient.class)
public interface ItemFeignClient {

    /**
     * 获取详情页数据
     * @param skuId  skuId
     * @return  Map<String,Object>
     */
    @GetMapping("/{skuId}")
    public Result<Map<String,Object>> getSkuId(@PathVariable Long skuId);

}
