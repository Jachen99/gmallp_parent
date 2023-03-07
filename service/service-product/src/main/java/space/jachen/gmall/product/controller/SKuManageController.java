package space.jachen.gmall.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.domain.product.SkuInfo;
import space.jachen.gmall.domain.product.SpuImage;
import space.jachen.gmall.domain.product.SpuSaleAttr;
import space.jachen.gmall.product.service.BaseSkuService;
import space.jachen.gmall.product.service.BaseSpuService;

import java.util.List;

/**
 * 商品后台 - 商品信息管理
 *  -- sku 商品属性sku管理
 *
 * @author JaChen
 * @date 2023/3/7 9:40
 */
@RestController
@RequestMapping("/admin/product")
public class SKuManageController {

    @Autowired
    private BaseSpuService baseSpuService;
    @Autowired
    private BaseSkuService baseSkuService;


    /**
     * 保存 skuInfo
     * @param skuInfo  SkuInfo
     *
     * @return  ok
     */
    @PostMapping("/saveSkuInfo")
    public Result<Object> saveSkuInfo(@RequestBody SkuInfo skuInfo){
        baseSkuService.saveSkuInfo(skuInfo);

        return Result.ok();
    }


    /**
     * 根据spuId 查询销售属性
     * @param spuId spuId
     *
     * @return  List<SpuSaleAttr>
     */
    @GetMapping("/spuSaleAttrList/{spuId}")
    public Result<List<SpuSaleAttr>> getSpuSaleAttrList(@PathVariable Long spuId){
        List<SpuSaleAttr> spuSaleAttrList = baseSpuService.getSpuSaleAttrList(spuId);

        return Result.ok(spuSaleAttrList);
    }



    /**
     * 根据spuId 获取spuImage 集合
     * @param spuId  spuId
     *
     * @return List<SpuImage>
     */
    @GetMapping("/spuImageList/{spuId}")
    public Result<List<SpuImage>> getSpuImageList(@PathVariable Long spuId){
        List<SpuImage> spuImageList = baseSpuService.getSpuImageList(spuId);

        return Result.ok(spuImageList);
    }


}