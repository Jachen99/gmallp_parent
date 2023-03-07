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

import java.util.List;
import java.util.function.Consumer;

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
    public List<SkuInfo> findSkuInfoBySkuIdList(Long skuId) {
        // 查找 SkuInfoList
        List<SkuInfo> skuInfoList = skuInfoMapper.selectList(
                new LambdaQueryWrapper<SkuInfo>() {{
                    eq(BaseEntity::getId, skuId);
                }}
        );
        if ( !CollectionUtils.isEmpty(skuInfoList) ){
            // 封装 skuImageList
            skuInfoList.forEach(skuInfo -> {
                List<SkuImage> skuImages = skuImageMapper.selectList(
                        new LambdaQueryWrapper<SkuImage>() {{
                            eq(SkuImage::getSkuId, skuId);
                        }}
                );
                skuInfo.setSkuImageList(skuImages);
            });
            // 封装 skuAttrValueList
            skuInfoList.forEach(skuInfo -> {
                List<SkuAttrValue> skuAttrValues = skuAttrValueMapper.selectList(
                        new LambdaQueryWrapper<SkuAttrValue>() {{
                            eq(SkuAttrValue::getSkuId, skuId);
                        }}
                );
                skuInfo.setSkuAttrValueList(skuAttrValues);
            });
            // 封装 skuSaleAttrValueList
            // 如果 SkuInfoList 存在 -- > 则为 skuInfoList 列表中的每个 SkuInfo 对象设置 skuSaleAttrValueList 属性
            Consumer<SkuInfo> skuInfoConsumer = skuInfo -> {
                List<SkuSaleAttrValue> skuSaleAttrValues = skuSaleAttrValueMapper.selectList(
                        new LambdaQueryWrapper<SkuSaleAttrValue>() {{
                            eq(SkuSaleAttrValue::getSkuId, skuId);
                        }}
                );
                skuInfo.setSkuSaleAttrValueList(skuSaleAttrValues);
            };
            skuInfoList.forEach(skuInfoConsumer);
        }

        return skuInfoList;
    }

    @Override
    public List<BaseAttrInfo> getAttrList(Long skuId) {
        // 根据 skuId 获取 sku_info 表中的 三级分类id category3Id
        List<SkuInfo> skuInfoList = skuInfoMapper.selectList(
                new LambdaQueryWrapper<SkuInfo>() {{
                    eq(BaseEntity::getId, skuId);
                }}
        );
        if ( !CollectionUtils.isEmpty(skuInfoList) ){
            for (SkuInfo skuInfo : skuInfoList) {
                Long category3Id = skuInfo.getCategory3Id();
                // 根据 category3Id 查找 平台属性基本表
                return baseAttrInfoMapper.selectList(
                        new LambdaQueryWrapper<BaseAttrInfo>() {{
                            eq(BaseAttrInfo::getCategoryId, category3Id)
                                    .orderByDesc(BaseAttrInfo::getId);
                        }}
                );
            }
        }
        return null;
    }

}
