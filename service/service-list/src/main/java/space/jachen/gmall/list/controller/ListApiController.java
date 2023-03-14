package space.jachen.gmall.list.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.list.service.SearchService;

/**
 * @author JaChen
 * @date 2023/3/14 11:44
 */
@RestController
@RequestMapping("/api/list")
public class ListApiController {

    @Autowired
    private SearchService searchService;


    /**
     * 更新商品的热度排名
     * @param skuId  skuId
     * @return Result
     */
    @GetMapping("/inner/incrHotScore/{skuId}")
    public Result<String> incrHotScore(@PathVariable Long skuId){
        searchService.incrHotScore(skuId);
        return Result.ok();
    }


    /**
     * 上架
     * @param skuId skuId
     * @return Result
     */
    @GetMapping("/inner/upperGoods/{skuId}")
    public Result<String> upperGoods(@PathVariable Long skuId){
        searchService.upperGoods(skuId);
        return Result.ok();
    }

    /**
     * 下架
     * @param skuId skuId
     * @return Result
     */
    @DeleteMapping("/inner/lowerGoods/{skuId}")
    public Result<String> lowerGoods(@PathVariable Long skuId){
        searchService.lowerGoods(skuId);
        return Result.ok();
    }

}
