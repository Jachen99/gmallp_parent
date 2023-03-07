package space.jachen.gmall.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import space.jachen.gmall.domain.product.SpuImage;
import space.jachen.gmall.domain.product.SpuInfo;
import space.jachen.gmall.domain.product.SpuSaleAttr;

import java.util.List;

/**
 * @author JaChen
 * @date 2023/3/6 9:52
 */
public interface BaseSpuService extends IService<SpuInfo> {

    /**
     * 获取spu分页列表
     * @param spuPage  分页对象
     * @param spuInfo  查询条件对象 category3Id
     *
     * @return   Page<SpuInfo>
     */
    IPage<SpuInfo> getSpuPageList(IPage<SpuInfo> spuPage, SpuInfo spuInfo);

    /**
     * 保存新增 spu对象
     * @param spuInfo  spuInfo
     */
    void saveSpuInfo(SpuInfo spuInfo);

    /**
     * 根据spuId 获取spuImage 集合
     * @param spuId  spuId
     */
    List<SpuImage> getSpuImageList(Long spuId);

    /**
     * 根据spuId 查询销售属性
     * @param spuId spuId
     *
     * @return  List<SpuSaleAttr>
     */
    List<SpuSaleAttr> getSpuSaleAttrList(Long spuId);

    List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId);
}
