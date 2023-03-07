package space.jachen.gmall.product.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.domain.product.BaseAttrInfo;
import space.jachen.gmall.domain.product.SkuInfo;
import space.jachen.gmall.product.service.BaseSkuService;

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