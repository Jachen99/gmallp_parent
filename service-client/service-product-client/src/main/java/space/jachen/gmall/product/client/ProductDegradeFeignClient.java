package space.jachen.gmall.product.client;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.domain.base.BaseCategoryView;
import space.jachen.gmall.domain.product.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/3/9 14:19
 */
@Component
public class ProductDegradeFeignClient implements ProductFeignClient{

    @Override
    public BaseTrademark getTrademark(Long tmId) {
        return null;
    }

    @Override
    public Result<List<JSONObject>> getBaseCategoryList() {
        return null;
    }

    @Override
    public Map<String, String> getSkuValueIdsMap(Long spuId) {
        return null;
    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId) {
        return null;
    }

    @Override
    public List<BaseAttrInfo> getAttrListBySkuId(Long skuId) {
        return null;
    }

    @Override
    public List<SpuPoster> findSpuPosterBySpuId(Long spuId) {
        return null;
    }

    @Override
    public BaseCategoryView getCategoryView(Long category3Id) {
        return null;
    }

    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        return null;
    }

    @Override
    public SkuInfo findSkuInfoBySkuId(Long skuId) {
        return null;
    }
}
