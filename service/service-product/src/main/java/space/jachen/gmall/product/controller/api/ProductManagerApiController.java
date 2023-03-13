package space.jachen.gmall.product.controller.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.domain.base.BaseCategoryView;
import space.jachen.gmall.domain.product.*;
import space.jachen.gmall.product.service.BaseCategoryService;
import space.jachen.gmall.product.service.BaseSkuService;
import space.jachen.gmall.product.service.BaseSpuService;
import space.jachen.gmall.product.service.BaseTrademarkService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前台 - 商品管理 控制器
 *    --- 商品详情 item 模块 api 接口
 *
 * @author JaChen
 * @date 2023/3/7 18:24
 */
@RestController
@RequestMapping("/api/product")
@SuppressWarnings("all")
public class ProductManagerApiController {

    @Autowired
    private BaseSkuService baseSkuService;
    @Autowired
    private BaseSpuService baseSpuService;
    @Autowired
    private BaseTrademarkService baseTrademarkService;
    @Autowired
    private BaseCategoryService baseCategoryService;


    /**
     * 获取首页分类数据
     * @return List<JSONObject>
     */
    @GetMapping("/getBaseCategoryList")
    public Result<List<JSONObject>> getBaseCategoryList(){
        List<JSONObject> list = baseCategoryService.getBaseCategoryList();
        return Result.ok(list);
    }


    /**
     * 根据spuId 获取到 销售属性值Id 与 skuId 组成的数据集  切换属性
     * @param spuId  spuId
     * @return Map
     */
    @GetMapping("/inner/getSkuValueIdsMap/{spuId}")
    public Map<String,String> getSkuValueIdsMap(@PathVariable Long spuId){
        Map<String,Object> mapList = baseSkuService.getSkuValueIdsMap(spuId);
        Map<String,String> result = new HashMap<>();
        result.put("value_ids", JSON.toJSONString(mapList));
        return result;
    }



    /**
     * 根据 spuId,skuId 获取销售属性数据  勾选属性
     * @param skuId  商品唯一编号 skuId
     * @param spuId  spuId
     *
     * @return  List<SpuSaleAttr>
     */
    @GetMapping("/inner/getSpuSaleAttrListCheckBySku/{skuId}/{spuId}")
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(
            @PathVariable Long skuId, @PathVariable Long spuId){
        return baseSpuService.getSpuSaleAttrListCheckBySku(skuId,spuId);
    }



    /**
     * 根据 skuId 获取平台属性数据
     * @param skuId 商品唯一编号 skuId
     *
     * @return  List<BaseAttrInfo>
     */
    @GetMapping("/inner/getAttrList/{skuId}")
    public List<BaseAttrInfo> getAttrListBySkuId(@PathVariable Long skuId){
        return baseSkuService.getAttrListBySkuId(skuId);
    }



    /**
     * 根据spuId 获取海报数据
     * @param spuId spuId
     * @return List<SpuPoster>
     */
    @GetMapping("/inner/findSpuPosterBySpuId/{spuId}")
    public List<SpuPoster> findSpuPosterBySpuId(@PathVariable Long spuId){
        return baseSpuService.findSpuPosterBySpuId(spuId);
    }



    /**
     * 根据三级分类id获取分类信息
     * @param category3Id  三级分类编号
     * @return BaseCategoryView
     */
    @PostMapping("/inner/getCategoryView/{category3Id}")
    public BaseCategoryView getCategoryView( @PathVariable Long category3Id){
        return baseCategoryService.getCategoryView(category3Id);
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
     * 根据 skuId 列表查询到 skuInfo 信息集合
     * @param skuId  商品唯一编号 skuId
     *
     * @return  List<SkuInfo>
     */
    @GetMapping("/inner/getSkuInfo/{skuId}")
    public SkuInfo findSkuInfoBySkuId(@PathVariable Long skuId){
       return baseSkuService.findSkuInfoBySkuId(skuId);
    }
}