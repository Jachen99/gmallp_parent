package space.jachen.gmall.product.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.domain.product.*;
import space.jachen.gmall.product.service.BaseSkuService;
import space.jachen.gmall.product.service.BaseSpuService;
import space.jachen.gmall.product.service.BaseTrademarkService;

import java.math.BigDecimal;
import java.util.List;

/**
 * 前台 - 商品管理 控制器
 *    --- 商品详情 item 模块 api 接口
 *
 * @author JaChen
 * @date 2023/3/7 18:24
 */
@RestController
@RequestMapping("/api/product")
public class ProductManagerApiController {


    @Autowired
    private BaseSkuService baseSkuService;
    @Autowired
    private BaseSpuService baseSpuService;
    @Autowired
    private BaseTrademarkService baseTrademarkService;


    /**
     * 根据三级分类Id集合 查询
     * @param categoryTrademarkVo  三级分类编号和品牌id集合 View
     * @return List<BaseCategory3>
     */
    @PostMapping("/inner/findBaseCategory3ByCategory3IdList")
    public Result<List<BaseCategory3>> findBaseCategory3ByCategory3IdList(@RequestBody CategoryTrademarkVo categoryTrademarkVo){
        List<BaseCategory3> baseCategory3List = baseTrademarkService
                .findBaseCategory3ByCategory3IdList(categoryTrademarkVo);

        return Result.ok(baseCategory3List);
    }




    /**
     * 根据品牌Id 获取品牌数据
     * @param tmId 品牌Id
     * @return  BaseTrademark
     */
    @GetMapping("/inner/getTrademark/{tmId}")
    public Result<BaseTrademark> getTrademark(@PathVariable Long tmId){
        BaseTrademark baseTrademarkResult = baseTrademarkService.getTrademark(tmId);

        return Result.ok(baseTrademarkResult);
    }



    /**
     * 根据spuId,skuId 获取销售属性数据
     * @param skuId  商品唯一编号 skuId
     * @param spuId  spuId
     *
     * @return  List<SpuSaleAttr>
     */
    @GetMapping("/getSpuSaleAttrListCheckBySku/{skuId}/{spuId}")
    public Result<List<SpuSaleAttr>> getSpuSaleAttrListCheckBySku(@PathVariable Long skuId, @PathVariable Long spuId){
        List<SpuSaleAttr> spuSaleAttrList = baseSpuService.getSpuSaleAttrListCheckBySku(skuId,spuId);

        return Result.ok(spuSaleAttrList);
    }



    /**
     * 根据skuId 获取最新的商品价格
     * @param skuId  商品唯一编号 skuId
     *
     * @return BigDecimal
     */
    @GetMapping("/inner/getSkuPrice/{skuId}")
    public BigDecimal getSkuPrice(@PathVariable Long skuId){

        return baseSkuService.getSkuPrice(skuId);
    }




    /**
     * 根据 skuId 获取平台属性数据
     * @param skuId 商品唯一编号 skuId
     *
     * @return  List<BaseAttrInfo>
     */
    @GetMapping("/inner/getAttrList/{skuId}")
    public Result<List<BaseAttrInfo>> getAttrListBySkuId(@PathVariable Long skuId){
        List<BaseAttrInfo> baseAttrInfoList = baseSkuService.getAttrListBySkuId(skuId);

        return Result.ok(baseAttrInfoList);
    }



    /**
     * 根据 skuId 列表查询到 skuInfo 信息集合
     * @param skuId  商品唯一编号 skuId
     *
     * @return  List<SkuInfo>
     */
    @GetMapping("/inner/getSkuInfo/{skuId}")
    public Result<List<SkuInfo>> findSkuInfoBySkuIdList(@PathVariable Long skuId){
        List<SkuInfo> skuInfoList = baseSkuService.findSkuInfoBySkuIdList(skuId);

        return Result.ok(skuInfoList);
    }
}