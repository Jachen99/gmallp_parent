package space.jachen.gmall.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import space.jachen.gmall.domain.product.SkuInfo;

/**
 * @author JaChen
 * @date 2023/3/7 11:15
 */
public interface BaseSkuService {
    /**
     * 保存 skuInfo
     * @param skuInfo  SkuInfo
     */
    void saveSkuInfo(SkuInfo skuInfo);

    /**
     * sku分页列表
     *
     * @return IPage<SkuInfo>
     */
    IPage<SkuInfo> getSkuListPage(IPage<SkuInfo> skuInfoIPage);

    /**
     * 商品上架
     * @param skuId  skuId
     */
    void onSale(Long skuId);

    /**
     * 商品下架
     * @param skuId  skuId
     */
    void cancelSale(Long skuId);
}
