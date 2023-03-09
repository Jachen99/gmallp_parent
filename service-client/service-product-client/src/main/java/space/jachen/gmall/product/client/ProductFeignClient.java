package space.jachen.gmall.product.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
 * @date 2023/3/9 14:16
 */
@FeignClient(value ="service-product", fallback = ProductDegradeFeignClient.class)
public interface ProductFeignClient {

    public static final String BaseURL = "/api/product";

    /**
     * 根据spuId 获取到 销售属性值Id 与 skuId 组成的数据集  切换属性
     * @param spuId  spuId
     * @return Map
     */
    @GetMapping(BaseURL+"/inner/getSkuValueIdsMap/{spuId}")
    public Map<String,Object> getSkuValueIdsMap(@PathVariable Long spuId);

    /**
     * 根据 spuId,skuId 获取销售属性数据  勾选属性
     * @param skuId  商品唯一编号 skuId
     * @param spuId  spuId
     *
     * @return  List<SpuSaleAttr>
     */
    @GetMapping(BaseURL+"/inner/getSpuSaleAttrListCheckBySku/{skuId}/{spuId}")
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(@PathVariable Long skuId, @PathVariable Long spuId);


    /**
     * 根据 skuId 获取平台属性数据
     * @param skuId 商品唯一编号 skuId
     *
     * @return  List<BaseAttrInfo>
     */
    @GetMapping(BaseURL+"/inner/getAttrList/{skuId}")
    public List<BaseAttrInfo> getAttrListBySkuId(@PathVariable Long skuId);


    /**
     * 根据spuId 获取海报数据
     * @param spuId spuId
     * @return List<SpuPoster>
     */
    @GetMapping(BaseURL+"/inner/findSpuPosterBySpuId/{spuId}")
    public List<SpuPoster> findSpuPosterBySpuId(@PathVariable Long spuId);


    /**
     * 根据三级分类id获取分类信息
     * @param category3Id  三级分类编号
     * @return BaseCategoryView
     */
    @PostMapping(BaseURL+"/inner/getCategoryView/{category3Id}")
    public BaseCategoryView getCategoryView(@PathVariable Long category3Id);


    /**
     * 根据skuId 获取最新的商品价格
     * @param skuId  商品唯一编号 skuId
     *
     * @return BigDecimal
     */
    @GetMapping(BaseURL+"/inner/getSkuPrice/{skuId}")
    public BigDecimal getSkuPrice(@PathVariable Long skuId);


    /**
     * 根据 skuId 列表查询到 skuInfo 信息集合
     * @param skuId  商品唯一编号 skuId
     *
     * @return  List<SkuInfo>
     */
    @GetMapping(BaseURL+"/inner/getSkuInfo/{skuId}")
    public SkuInfo findSkuInfoBySkuId(@PathVariable Long skuId);
}
