package space.jachen.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import space.jachen.gmall.domain.base.BaseEntity;
import space.jachen.gmall.domain.product.*;
import space.jachen.gmall.product.mapper.*;
import space.jachen.gmall.product.service.BaseSkuService;

import java.math.BigDecimal;
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
    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;
    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;

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

    @Override
    public SkuInfo findSkuInfoBySkuId(Long skuId) {

        // 查找 SkuInfo
        SkuInfo skuInfo = skuInfoMapper.selectOne(
                new LambdaQueryWrapper<SkuInfo>() {{
                    eq(BaseEntity::getId, skuId);
                }}
        );
        if (skuInfo != null){
            List<SkuImage> skuImageList = skuImageMapper.selectList(
                    new LambdaQueryWrapper<SkuImage>() {{
                        eq(SkuImage::getSkuId, skuInfo.getId());
                    }}
            );
            skuInfo.setSkuImageList(skuImageList);
        }

        return skuInfo;
    }

    @Override
    public List<BaseAttrInfo> getAttrListBySkuId(Long skuId) {
        // 根据 skuId 获取 sku_info 表中的 三级分类id category3Id
        SkuInfo info = skuInfoMapper.selectOne(
                new LambdaQueryWrapper<SkuInfo>() {{
                    eq(BaseEntity::getId, skuId);
                }}
        );
        if ( null != info ){
            Long category3Id = info.getCategory3Id();
            // 根据 category3Id 查找 平台属性基本表
            List<BaseAttrInfo> baseAttrInfoList = baseAttrInfoMapper.selectList(
                    new LambdaQueryWrapper<BaseAttrInfo>() {{
                        eq(BaseAttrInfo::getCategoryId, category3Id)
                                .orderByDesc(BaseAttrInfo::getId);
                    }}
            );
            // 获取 attrValueList 并存入 baseAttrInfoList 集合返回
            if ( !CollectionUtils.isEmpty(baseAttrInfoList) ){
                for (BaseAttrInfo attrInfo : baseAttrInfoList) {
                    // 获取 平台属性id
                    Long attrInfoId = attrInfo.getId();
                    List<BaseAttrValue> baseAttrValueList = baseAttrValueMapper.selectList(
                            new LambdaQueryWrapper<BaseAttrValue>() {{
                                eq(BaseAttrValue::getAttrId, attrInfoId);
                            }}
                    );
                    attrInfo.setAttrValueList(baseAttrValueList);
                }
            }
            return baseAttrInfoList;
        }
        return null;
    }

    @Override
    public BigDecimal getSkuPrice(Long skuId) {
        SkuInfo skuInfo = skuInfoMapper.selectOne(
                new LambdaQueryWrapper<SkuInfo>() {{
                    eq(BaseEntity::getId, skuId);
                }}
        );
        if ( skuInfo != null){
            return skuInfo.getPrice();
        }
        return new BigDecimal(0);
    }


}
