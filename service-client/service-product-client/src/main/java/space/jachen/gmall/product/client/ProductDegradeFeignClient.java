package space.jachen.gmall.product.client;

import org.springframework.stereotype.Component;
import space.jachen.gmall.domain.base.BaseCategoryView;
import space.jachen.gmall.domain.product.BaseAttrInfo;
import space.jachen.gmall.domain.product.SkuInfo;
import space.jachen.gmall.domain.product.SpuPoster;
import space.jachen.gmall.domain.product.SpuSaleAttr;

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
