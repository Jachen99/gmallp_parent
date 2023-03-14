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
     * 上架
     * @param skuId skuId
     * @return
     */
    @GetMapping("/inner/upperGoods/{skuId}")
    public Result<String> upperGoods(@PathVariable Long skuId){
        searchService.upperGoods(skuId);
        return Result.ok();
    }

    /**
     * 下架
     * @param skuId skuId
     * @return
     */
    @DeleteMapping("/inner/lowerGoods/{skuId}")
    public Result<String> lowerGoods(@PathVariable Long skuId){
        searchService.lowerGoods(skuId);
        return Result.ok();
    }

}
