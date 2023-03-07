package space.jachen.gmall.product.service;

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
}
