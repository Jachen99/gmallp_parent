package space.jachen.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import space.jachen.gmall.domain.product.SkuAttrValue;
import space.jachen.gmall.domain.product.SkuImage;
import space.jachen.gmall.domain.product.SkuInfo;
import space.jachen.gmall.domain.product.SkuSaleAttrValue;
import space.jachen.gmall.product.mapper.SkuAttrValueMapper;
import space.jachen.gmall.product.mapper.SkuImageMapper;
import space.jachen.gmall.product.mapper.SkuInfoMapper;
import space.jachen.gmall.product.mapper.SkuSaleAttrValueMapper;
import space.jachen.gmall.product.service.BaseSkuService;

import java.util.List;

/**
 * @author JaChen
 * @date 2023/3/7 11:17
 */
@Service
public class BaseSkuServiceImpl implements BaseSkuService {

    @Autowired
    private SkuInfoMapper skuInfoMapper;
    @Autowired
    private SkuImageMapper skuImageMapper;
    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;
    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        // 保存基本信息
        skuInfoMapper.insert(skuInfo);
        // 保存图片信息
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if ( !CollectionUtils.isEmpty(skuImageList) ){
            skuImageList.forEach(skuImage -> {
                // 设置 skuId 字段
                skuImage.setSkuId(skuInfo.getId());
                // 存入数据库
                skuImageMapper.insert(skuImage);
            });
        }
        // 保存Sku平台属性值
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        if ( !CollectionUtils.isEmpty(skuAttrValueList)){
            skuAttrValueList.forEach(skuAttrValue -> {
                // 设置 skuId
                skuAttrValue.setSkuId(skuInfo.getId());
                // 存入数据库
                skuAttrValueMapper.insert(skuAttrValue);
            });
        }
        // 保存Sku销售属性值
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        if ( !CollectionUtils.isEmpty(skuSaleAttrValueList)){
            skuSaleAttrValueList.forEach(skuSaleAttrValue -> {
                // 设置skuId 和 SpuId
                skuSaleAttrValue.setSkuId(skuInfo.getId());
                skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
                // 存入数据库
                skuSaleAttrValueMapper.insert(skuSaleAttrValue);
            });
        }
    }

    @Override
    public IPage<SkuInfo> getSkuListPage(IPage<SkuInfo> skuInfoIPage) {

        return skuInfoMapper.selectPage(skuInfoIPage,null);
    }

    @Override
    public void onSale(Long skuId) {

        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(skuId);
        skuInfo.setIsSale(1);

        skuInfoMapper.updateById(skuInfo);
    }

    @Override
    public void cancelSale(Long skuId) {

        SkuInfo skuInfo = new SkuInfo();
        skuInfo.setId(skuId);
        skuInfo.setIsSale(0);

        skuInfoMapper.updateById(skuInfo);
    }

}
