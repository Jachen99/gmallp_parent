package space.jachen.gmall.activity.controller;

/**
 * @author JaChen
 * @date 2023/3/24 20:05
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.gmall.activity.service.SeckillGoodsService;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.domain.activity.SeckillGoods;
import space.jachen.gmall.product.client.ProductFeignClient;
import space.jachen.gmall.user.client.UserFeignClient;

import java.util.List;

@RestController
@RequestMapping("/api/activity/seckill")
@SuppressWarnings("all")
public class SeckillGoodsApiController {

    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private ProductFeignClient productFeignClient;


    /**
     * 返回全部列表
     * @return
     */
    @GetMapping("/findAll")
    public Result<List<SeckillGoods>> findAll() {
        List<SeckillGoods> serviceAll = seckillGoodsService.findAll();
        return Result.ok(serviceAll);
    }


    /**
     * 获取实体
     * @param skuId
     * @return
     */
    @GetMapping("/getSeckillGoods/{skuId}")
    public Result<SeckillGoods> getSeckillGoods(@PathVariable Long skuId) {
        return Result.ok(seckillGoodsService.getSeckillGoods(skuId));
    }
}

