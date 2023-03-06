package space.jachen.gmall.product.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.domain.product.BaseTrademark;
import space.jachen.gmall.domain.product.CategoryTrademarkVo;
import space.jachen.gmall.product.service.BaseCategoryTrademarkService;

import java.util.List;

/**
 * 商品后台 - 基本信息管理
 *  -- 分类品牌
 *
 * @author jachen
 * @since 2023-03-06
 */
@RestController
@RequestMapping("/admin/product/baseCategoryTrademark")
public class BaseCategoryTrademarkController {

    @Autowired
    private BaseCategoryTrademarkService baseCategoryTrademarkService;


    /**
     * 保存分类品牌关联
     * @param categoryTrademarkVo  三级分类编号和品牌id集合
     *
     * @return Result<Object>
     */
    @PostMapping("/save")
    public Result<Object> saveTrademark(@RequestBody CategoryTrademarkVo categoryTrademarkVo){
        baseCategoryTrademarkService.saveTrademark(categoryTrademarkVo);

        return Result.ok();
    }


    /**
     * 根据category3Id获取可选品牌列表
     * @param category3Id  三级分类id
     *
     * @return List<BaseTrademark>
     */
    @GetMapping("/findCurrentTrademarkList/{category3Id}")
    public Result<List<BaseTrademark>> findCurrentTrademarkList(@PathVariable Long category3Id){
        List<BaseTrademark> baseTrademarkList = baseCategoryTrademarkService.findCurrentTrademarkList(category3Id);

        return Result.ok(baseTrademarkList);
    }


    /**
     * 根据category3Id获取品牌列表
     * @param category3Id 三级分类的id
     *
     * @return List<BaseTrademark>
     */
    @GetMapping("/findTrademarkList/{category3Id}")
    public Result<List<BaseTrademark>> findTrademarkList(@PathVariable Long category3Id){
        List<BaseTrademark> baseTrademarkList = baseCategoryTrademarkService.findTrademarkList(category3Id);

        return Result.ok(baseTrademarkList);
    }

}

