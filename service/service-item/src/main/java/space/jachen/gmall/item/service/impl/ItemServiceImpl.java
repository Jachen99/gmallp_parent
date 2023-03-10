package space.jachen.gmall.item.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
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
import java.util.stream.Collectors;

/**
 * @author JaChen
 * @date 2023/3/8 10:10
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    @Qualifier("space.jachen.gmall.product.client.ProductFeignClient")
    private ProductFeignClient productFeignClient;

    @Override
    public Map<String, Object> getSkuId(Long skuId) {
        // 1、创建结果集
        HashMap<String, Object> resultMap = new HashMap<>();
        // 2、封装 商品详情数据
        // 2.1、封装 sku平台属性List
        List<BaseAttrInfo> attrInfoList = productFeignClient.getAttrListBySkuId(skuId);
        // 封装前台需要的信息 skuAttr.attrName skuAttr.attrValue
        List<Map<String, String>> collect = attrInfoList.stream().map(attrListBySkuId -> {
            Map<String, String> attrMap = new HashMap<>();
            String attrName = attrListBySkuId.getAttrName();
            attrMap.put("attrName", attrName);
            String valueName = attrListBySkuId.getAttrValueList().get(0).getValueName();
            attrMap.put("attrValue", valueName);
            return attrMap;
        }).collect(Collectors.toList());
        resultMap.put("skuAttrList",collect);
        // 2.2、封装 最新价格
        BigDecimal skuPrice = productFeignClient.getSkuPrice(skuId);
        resultMap.put("skuPrice",skuPrice);
        SkuInfo skuInfo = productFeignClient.findSkuInfoBySkuId(skuId);
        if (null != skuInfo){
            // 2.3、封装 skuInfo
            resultMap.put("skuInfo",skuInfo);
            // 获取 三级分类id
            Long category3Id = skuInfo.getCategory3Id();
            // 获取 spuId
            Long spuId = skuInfo.getSpuId();
            // 2.4、封装 spu海报数据
            List<SpuPoster> spuPosterList = productFeignClient.findSpuPosterBySpuId(spuId);
            resultMap.put("spuPosterList",spuPosterList);
            // 2.5、封装 三级分类id 获取 categoryView
            BaseCategoryView categoryView = productFeignClient.getCategoryView(category3Id);
            resultMap.put("categoryView",categoryView);
            // 2.6、封装 spu销售属性
            List<SpuSaleAttr> spuSaleAttrList = productFeignClient.getSpuSaleAttrListCheckBySku(skuId, spuId);
            resultMap.put("spuSaleAttrList",spuSaleAttrList);
            // 2.7、封装 查询销售属性值Id 与skuId 组合的map
            Map<String, String> skuValueIdsMap = productFeignClient.getSkuValueIdsMap(spuId);
            resultMap.put("valuesSkuJson",skuValueIdsMap.get("value_ids"));
        }

        return resultMap;
    }
}
