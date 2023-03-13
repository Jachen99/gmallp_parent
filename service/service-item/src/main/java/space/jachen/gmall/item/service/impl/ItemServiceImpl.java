package space.jachen.gmall.item.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.jachen.gmall.common.config.ThreadPoolConfig;
import space.jachen.gmall.domain.base.BaseCategoryView;
import space.jachen.gmall.domain.product.BaseAttrInfo;
import space.jachen.gmall.domain.product.SkuInfo;
import space.jachen.gmall.domain.product.SpuPoster;
import space.jachen.gmall.domain.product.SpuSaleAttr;
import space.jachen.gmall.item.service.ItemService;
import space.jachen.gmall.product.client.ProductFeignClient;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author JaChen
 * @date 2023/3/8 10:10
 */
@Service
@SuppressWarnings("all")
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ProductFeignClient productFeignClient;
    @Autowired
    private ThreadPoolConfig threadPoolConfig;

    @Override
    public Map<String, Object> getSkuId(Long skuId) {

        // 创建结果集
        HashMap<String, Object> resultMap = new HashMap<>();
        // 获取线程池
        ThreadPoolExecutor executor = threadPoolConfig.executor();
        // 异步编排优化
        // 1、获取异步对象返回结果
        CompletableFuture future = new CompletableFuture<>();
        CompletableFuture<SkuInfo> skuInfoFuture = future.supplyAsync(() -> {
            SkuInfo skuInfo = productFeignClient.findSkuInfoBySkuId(skuId);
            //  封装 skuInfo
            resultMap.put("skuInfo", skuInfo);
            return skuInfo;
        },executor);
        // 2、通过 skuInfoFuture 获取需要数据
        // 封装 spu海报数据
        CompletableFuture<Void> spuPosterListFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            List<SpuPoster> spuPosterList = productFeignClient.findSpuPosterBySpuId(skuInfo.getSpuId());
            resultMap.put("spuPosterList", spuPosterList);
        },executor);

        // 封装 三级分类id 获取 categoryView
        CompletableFuture<Void> categoryViewFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            BaseCategoryView categoryView = productFeignClient.getCategoryView(skuInfo.getCategory3Id());
            resultMap.put("categoryView", categoryView);
        },executor);

        // 封装 spu销售属性
        CompletableFuture<Void> spuSaleAttrListFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            List<SpuSaleAttr> spuSaleAttrList = productFeignClient.getSpuSaleAttrListCheckBySku(skuId, skuInfo.getSpuId());
            resultMap.put("spuSaleAttrList", spuSaleAttrList);
        },executor);

        // 封装 查询销售属性值Id 与skuId 组合的map
        CompletableFuture<Void> valuesSkuJsonFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            Map<String, String> skuValueIdsMap = productFeignClient.getSkuValueIdsMap(skuInfo.getSpuId());
            resultMap.put("valuesSkuJson", skuValueIdsMap.get("value_ids"));
        },executor);

        // 封装 sku平台属性List
        CompletableFuture<Void> skuAttrListFuture = future.runAsync(() -> {
            List<BaseAttrInfo> attrInfoList = productFeignClient.getAttrListBySkuId(skuId);
            if (attrInfoList != null) {
                // 封装前台需要的信息 skuAttr.attrName skuAttr.attrValue
                List<Map<String, String>> collect = attrInfoList.stream().map(attrListBySkuId -> {
                    Map<String, String> attrMap = new HashMap<>();
                    String attrName = attrListBySkuId.getAttrName();
                    attrMap.put("attrName", attrName);
                    String valueName = attrListBySkuId.getAttrValueList().get(0).getValueName();
                    attrMap.put("attrValue", valueName);
                    return attrMap;
                }).collect(Collectors.toList());
                resultMap.put("skuAttrList", collect);
            }
        },executor);

        // 封装 最新价格
        CompletableFuture<Void> skuPriceFuture = future.runAsync(() -> {
            BigDecimal skuPrice = productFeignClient.getSkuPrice(skuId);
            resultMap.put("skuPrice", skuPrice);
        },executor);

        // 汇总后一起返回
        future.allOf(skuInfoFuture,spuPosterListFuture,categoryViewFuture,spuSaleAttrListFuture,
                valuesSkuJsonFuture,skuAttrListFuture,skuPriceFuture).join();

        return resultMap;
    }
}
