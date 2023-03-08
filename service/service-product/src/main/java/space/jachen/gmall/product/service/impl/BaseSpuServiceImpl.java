package space.jachen.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import space.jachen.gmall.domain.product.*;
import space.jachen.gmall.product.mapper.*;
import space.jachen.gmall.product.service.BaseSpuService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JaChen
 * @date 2023/3/6 9:56
 */
@Service
public class BaseSpuServiceImpl extends ServiceImpl<SpuInfoMapper,SpuInfo> implements BaseSpuService {

    @Autowired
    private SpuInfoMapper spuInfoMapper;
    @Autowired
    private SpuImageMapper spuImageMapper;
    @Autowired
    private SpuPosterMapper spuPosterMapper;
    @Autowired
    private SpuSaleAttrMapper spuSaleAttrMapper;
    @Autowired
    private SpuSaleAttrValueMapper spuSaleAttrValueMapper;
    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Override
    public IPage<SpuInfo> getSpuPageList(IPage<SpuInfo> spuPage, SpuInfo spuInfo) {
        LambdaQueryWrapper<SpuInfo> wrapper = new LambdaQueryWrapper<SpuInfo>() {{
            eq(SpuInfo::getCategory3Id, spuInfo.getCategory3Id())
                    // 根据id排序
                    .orderByDesc(SpuInfo::getId);
        }};
        // 查询
        return spuInfoMapper.selectPage(spuPage, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveSpuInfo(SpuInfo spuInfo) {
        // 存 base spu基本信息
        spuInfoMapper.insert(spuInfo);

        // 存 spuImage spu图片信息
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        if ( !CollectionUtils.isEmpty(spuImageList)){
            // 使用流式处理 遍历集合
            spuImageList.forEach(spuImage ->{
                spuImage.setSpuId(spuInfo.getId());
                spuImageMapper.insert(spuImage);
            });
        }
        // 存 spuPoster 海报信息
        List<SpuPoster> spuPosterList = spuInfo.getSpuPosterList();
        if ( !CollectionUtils.isEmpty(spuPosterList)){
            for (SpuPoster spuPoster : spuPosterList) {
                spuPoster.setSpuId(spuInfo.getId());

                spuPosterMapper.insert(spuPoster);
            }
        }
        // 存 SpuSaleAttr spu销售属性信息
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        if ( !CollectionUtils.isEmpty(spuSaleAttrList)){
            for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
                spuSaleAttr.setSpuId(spuInfo.getId());
                spuSaleAttrMapper.insert(spuSaleAttr);

                // 获取属性对应的属性值信息，并存入数据库
                List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
                if ( !CollectionUtils.isEmpty(spuSaleAttrValueList)){
                    for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValueList) {
                        spuSaleAttrValue.setSpuId(spuInfo.getId());
                        // 要设置 spuSaleAttrValue 表里的 属性 name 字段
                        spuSaleAttrValue.setSaleAttrName(spuSaleAttr.getSaleAttrName());
                        spuSaleAttrValueMapper.insert(spuSaleAttrValue);
                    }
                }
            }
        }
    }

    @Override
    public List<SpuImage> getSpuImageList(Long spuId) {

        return spuImageMapper.selectList(
                new LambdaQueryWrapper<SpuImage>(){{
                    eq(SpuImage::getSpuId,spuId);
                }}
        );

    }

    @Override
    public List<SpuSaleAttr> getSpuSaleAttrList(Long spuId) {
        // 根据 spuId 查 SpuSaleAttr 表中的数据
        List<SpuSaleAttr> spuSaleAttrList = spuSaleAttrMapper.selectList(
                new LambdaQueryWrapper<SpuSaleAttr>() {{
                    eq(SpuSaleAttr::getSpuId, spuId);
                }}
        );
        // 在 spuSaleAttrList 根据 base_sale_attr_id 查询
        if ( !CollectionUtils.isEmpty(spuSaleAttrList)){
            spuSaleAttrList.forEach(spuSaleAttr -> {
                List<SpuSaleAttrValue> spuSaleAttrValues = spuSaleAttrValueMapper
                        .selectList(
                                // 关联 getBaseSaleAttrId 和 getSpuId 查询
                                new LambdaQueryWrapper<SpuSaleAttrValue>() {{
                                    eq(SpuSaleAttrValue::getBaseSaleAttrId, spuSaleAttr.getBaseSaleAttrId())
                                            .eq(SpuSaleAttrValue::getSpuId, spuSaleAttr.getSpuId());
                                }}
                        );
                // 设置 spuSaleAttrValues
                spuSaleAttr.setSpuSaleAttrValueList(spuSaleAttrValues);
                }
            );
        }
        return spuSaleAttrList;
    }

    /**
     * 根据 spuId,skuId 获取销售属性数据
     * @param skuId  商品唯一编号 skuId
     * @param spuId  spuId
     * -----------------------------------------------------
     * SELECT sa.id ,sa.spu_id, sa.sale_attr_name,sa.base_sale_attr_id,
     *        sv.id sale_attr_value_id,
     *        sv.sale_attr_value_name,
     *        skv.sku_id,
     *        IF(skv.sku_id IS NULL,0,1)  is_checked
     * FROM spu_sale_attr sa
     *          INNER JOIN spu_sale_attr_value  sv ON  sa.spu_id=sv.spu_id AND sa.base_sale_attr_id=sv.base_sale_attr_id
     *          LEFT JOIN sku_sale_attr_value skv ON skv.sale_attr_value_id= sv.id AND skv.sku_id=23
     * WHERE  sa.spu_id=19
     * ORDER BY sv.base_sale_attr_id,sv.id
     * -------------------------------------------------------------
     *  spu_sale_attr  spu_sale_attr_value    sku_sale_attr_value（有spu_id字段）
     *
     * @return  List<SpuSaleAttr>
     */
    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(Long skuId, Long spuId) {
        // 1、根据 spuId 查询销售属性集合
        LambdaQueryWrapper<SpuSaleAttr> attrWrapper = new LambdaQueryWrapper<SpuSaleAttr>() {{
            eq(SpuSaleAttr::getSpuId, spuId);
        }};
        List<SpuSaleAttr> spuSaleAttrs = spuSaleAttrMapper.selectList(attrWrapper);
        spuSaleAttrs.forEach(spuSaleAttr -> {
            // 2、根据 spuId 获取销售属性值集合
            LambdaQueryWrapper<SpuSaleAttrValue> valueWrapper = new LambdaQueryWrapper<SpuSaleAttrValue>() {{
                eq(SpuSaleAttrValue::getSpuId, spuId);
            }};
            List<SpuSaleAttrValue> spuSaleAttrValues = spuSaleAttrValueMapper.selectList(valueWrapper);
            // 3、创建 返回结果集中的子结果集 spuSaleValueList 存放 spuSaleAttrValue 结果集
            List<SpuSaleAttrValue> spuSaleValueList = new ArrayList<>();
            spuSaleAttrValues.forEach(spuSaleAttrValue -> {
            /* 根据spu_sale_attr_value的id去sku_sale_attr_value找对应的sale_attr_value_id，
            在sku_id = 传入的sku_id 条件下 对比 是否存在  如果不存在 checked 设为0 否则为1 */
                // 4、根据 valueId 查询 spu_sale_attr_value 表
                Long valueId = spuSaleAttrValue.getId();
                LambdaQueryWrapper<SkuSaleAttrValue> valueIdWrapper = new LambdaQueryWrapper<SkuSaleAttrValue>() {{
                    eq(SkuSaleAttrValue::getSaleAttrValueId, valueId);
                }};
                // 要返回 list 因为可能对应多个sale_attr_value_id值
                List<SkuSaleAttrValue> skuSaleAttrValueList = skuSaleAttrValueMapper.selectList(valueIdWrapper);
                skuSaleAttrValueList.forEach(skuSaleAttrValue -> {
                    // 5、判断 sku_id 是否选中 ，如果选中 checked = 1；
                    if (skuId.equals(skuSaleAttrValue.getSkuId())){
                        spuSaleAttrValue.setIsChecked("1");
                    }else {
                        spuSaleAttrValue.setIsChecked("0");
                    }
                    spuSaleValueList.add(spuSaleAttrValue);
                    // spuSaleAttr.setSaleAttrName(spuSaleAttrValue.getSaleAttrName());
                });
            });
            // 6、封装 spuSaleAttrs
            spuSaleAttr.setSpuSaleAttrValueList(spuSaleValueList);
        });
        return spuSaleAttrs;
    }

    @Override
    public List<SpuPoster> findSpuPosterBySpuId(Long spuId) {
        return spuPosterMapper.selectList(
                new LambdaQueryWrapper<SpuPoster>(){{
                    eq(SpuPoster::getSpuId,spuId);
                }}
        );
    }
}
