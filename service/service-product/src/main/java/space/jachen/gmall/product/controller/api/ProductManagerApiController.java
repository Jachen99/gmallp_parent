package space.jachen.gmall.product.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.domain.product.SkuInfo;
import space.jachen.gmall.product.service.BaseSkuService;

import java.util.List;

/**
 * 前台 - 商品管理 控制器
 *    --- 商品详情 item 模块 api 接口
 *
 * @author JaChen
 * @date 2023/3/7 18:24
 */
@RestController
@RequestMapping("/api/product/inner")
public class ProductManagerApiController {


    @Autowired
    private BaseSkuService baseSkuService;

    /**
     * 根据 skuId 列表查询到 skuInfo 信息集合
     * @param skuId  商品唯一编号 skuId
     *
     * @return  List<SkuInfo>
     */
    @GetMapping("/getAttrList/{skuId}")
    public Result<List<SkuInfo>> findSkuInfoBySkuIdList(@PathVariable Long skuId){
        List<SkuInfo> skuInfoList = baseSkuService.findSkuInfoBySkuIdList(skuId);

        return Result.ok(skuInfoList);
    }
}