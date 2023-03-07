package space.jachen.gmall.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public class SkuManageController {

    @Autowired
    private BaseSpuService baseSpuService;
    @Autowired
    private BaseSkuService baseSkuService;


    /**
     * sku分页列表
     * @param page  当前页
     * @param limit  每页条数
     *
     * @return IPage<SkuInfo>
     */
    @GetMapping("/list/{page}/{limit}")
    public Result<IPage<SkuInfo>> getSkuListPage(@PathVariable Long page, @PathVariable Long limit){
        // 封装分页对象
        IPage<SkuInfo> skuInfoIPage = new Page<>(page,limit);
        skuInfoIPage = baseSkuService.getSkuListPage(skuInfoIPage);

        return Result.ok(skuInfoIPage);
    }


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
