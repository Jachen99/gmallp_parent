package space.jachen.gmall.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import space.jachen.gmall.domain.product.BaseAttrInfo;
import space.jachen.gmall.domain.product.SkuInfo;

import java.util.List;

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

    /**
     * item商品详情接口
     *   - 根据 skuId 列表查询到 skuInfo 信息集合
     * @param skuId  商品唯一编号 skuId
     * @return List<SkuInfo>
     */
    List<SkuInfo> findSkuInfoBySkuIdList(Long skuId);

    /**
     * item商品详情接口
     *      - 根据 skuId 获取平台属性数据
     * @param skuId 商品唯一编号 skuId
     *
     * @return  List<BaseAttrInfo>
     */
    List<BaseAttrInfo> getAttrList(Long skuId);
}
