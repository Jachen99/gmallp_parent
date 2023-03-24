package space.jachen.gmall.activity.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.domain.activity.SeckillGoods;

import java.util.List;

/**
 * @author JaChen
 * @date 2023/3/24 20:07
 */
@FeignClient(value = "service-activity",fallback = ActivityDegradeFeignClient.class)
public interface ActivityFeignClient {


    public static final String BaseURL = "/api/activity/seckill";


    /**
     * 返回全部列表
     * @return
     */
    @GetMapping(BaseURL+"/findAll")
    public Result<List<SeckillGoods>> findAll() ;


    /**
     * 获取实体
     * @param skuId
     * @return
     */
    @GetMapping(BaseURL+"/getSeckillGoods/{skuId}")
    public Result<SeckillGoods> getSeckillGoods(@PathVariable Long skuId) ;

}
