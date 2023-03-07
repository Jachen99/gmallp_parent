package space.jachen.gmall.domain.item;

import lombok.Data;
import space.jachen.gmall.domain.base.BaseCategoryView;
import space.jachen.gmall.domain.product.SkuInfo;
import space.jachen.gmall.domain.product.SpuPoster;
import space.jachen.gmall.domain.product.SpuSaleAttr;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 商品详情 ItemVo 对象
 *
 */
@Data
public class ItemVo {

    /**
     * sku信息
     */
    private SkuInfo skuInfo;
    /**
     * 分类信息
     */
    private BaseCategoryView categoryView;
    /**
     * spu销售属性
     */
    private List<SpuSaleAttr> spuSaleAttrList;
    /**
     * spu海报数据
     */
    private List<SpuPoster> spuPosterList;
    /**
     * sku平台属性
     */
    private List<Map<String, String>> skuAttrList;
    /**
     * 切换数据
     */
    private String valuesSkuJson;
    /**
     * 最新价格
     */
    private BigDecimal skuPrice;

}
