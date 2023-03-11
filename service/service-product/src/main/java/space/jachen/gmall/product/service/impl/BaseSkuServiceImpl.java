package space.jachen.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import space.jachen.gmall.common.cache.GmallCache;
import space.jachen.gmall.domain.base.BaseEntity;
import space.jachen.gmall.domain.product.*;
import space.jachen.gmall.product.mapper.*;
import space.jachen.gmall.product.service.BaseSkuService;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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
    @GmallCache(front = "skuInfo:")
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

    /**
     * item商品详情接口 - 根据 skuId 获取平台属性数据
     * -----------------------------------------
     * #根据表 sku_attr_value 关联 base_attr_info、base_attr_value 进行查询
     * SELECT
     *     bai.id,
     *     bai.attr_name,
     *     bai.category_id,
     *     bai.category_level,
     *     bav.id attr_value_id,
     *     bav.value_name,
     *     bav.attr_id
     * FROM
     *     base_attr_info bai
     *         INNER JOIN base_attr_value bav ON bai.id = bav.attr_id
     *         INNER JOIN sku_attr_value sav ON sav.value_id = bav.id
     * WHERE
     *         sav.sku_id = 23
     * @param skuId 商品唯一编号 skuId
     * ----------------------------------------------------------------
     * @return   List<BaseAttrInfo>
     */
    @Override
    @GmallCache(front = "attrList:")
    public List<BaseAttrInfo> getAttrListBySkuId(Long skuId) {
        // 1、创建封装结果集
        List<BaseAttrInfo> resultList = new ArrayList<>();
        // 2、根据 skuId 获取 skuAttrValueList 集合
        List<SkuAttrValue> skuAttrValueList = skuAttrValueMapper.selectList(
                new LambdaQueryWrapper<SkuAttrValue>() {{
                    eq(SkuAttrValue::getSkuId, skuId);
                }}
        );
        // 3、获取 BaseAttrInfo 数据集合
        skuAttrValueList.forEach(skuAttrValue -> {
            // 创建结果集中的valueList
            List<BaseAttrValue> valueList = new ArrayList<>();
            // 获取集合中的attr_id
            Long attrId = skuAttrValue.getAttrId();
            // 获取集合中的 value_id
            Long valueId = skuAttrValue.getValueId();
            // 封装查询条件 sku_attr_value.attr_id = base_attr_info.id
            LambdaQueryWrapper<BaseAttrInfo> attrWrapper = new LambdaQueryWrapper<BaseAttrInfo>() {{
                eq(BaseEntity::getId, attrId);
            }};
            // 封装查询条件 sku_attr_value.value_id = base_attr_value.id
            LambdaQueryWrapper<BaseAttrValue> valueWrapper = new LambdaQueryWrapper<BaseAttrValue>() {{
                eq(BaseEntity::getId, valueId);
            }};
            // 获取 BaseAttrInfo
            BaseAttrInfo attrInfo = baseAttrInfoMapper.selectOne(attrWrapper);
            // 获取 BaseAttrValue
            BaseAttrValue baseAttrValue = baseAttrValueMapper.selectOne(valueWrapper);
            // 4、封装结果集
            valueList.add(baseAttrValue);
            attrInfo.setAttrValueList(valueList);
            resultList.add(attrInfo);
        });

        return resultList;
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

    /**
     * 根据 spuId 获取到销售属性值Id 与 skuId 组成的数据集
     * @param spuId  spuId
     * ------------------------------------------------
     * SELECT sku_id  , GROUP_CONCAT(sale_attr_value_id  ORDER BY sp.base_sale_attr_id ASC SEPARATOR '|') value_ids
     *      FROM  `sku_sale_attr_value` sv
     *      INNER JOIN `spu_sale_attr_value` sp on sp.id = sv.sale_attr_value_id
     *      WHERE sv.spu_id=#{spuId}
     *      GROUP BY sku_id
     * ----------------------------
     * @return  Map<String,Object>
     */
    @Override
    @GmallCache(front = "skuValueIds:")
    public Map<String,Object> getSkuValueIdsMap(Long spuId) {
        // 1、创建 结果集 Map
        Map<String, Object> resultMap = new HashMap<>();
        // 2、根据 spuId 获取 sku_sale_attr_value 表数据
        LambdaQueryWrapper<SkuSaleAttrValue> spuIdWrapper = new LambdaQueryWrapper<SkuSaleAttrValue>() {{
            eq(SkuSaleAttrValue::getSpuId, spuId);
        }};
        List<SkuSaleAttrValue> skuSaleAttrValues = skuSaleAttrValueMapper.selectList(spuIdWrapper);
        // 3、获取去重的 sku_id
        Set<Long> skuIds = skuSaleAttrValues.stream().map(SkuSaleAttrValue::getSkuId).collect(Collectors.toSet());
        skuIds.forEach(skuId->{
            LambdaQueryWrapper<SkuSaleAttrValue> skuIdWrapper = new LambdaQueryWrapper<SkuSaleAttrValue>() {{
                eq(SkuSaleAttrValue::getSkuId, skuId);
            }};
            // 4、根据 skuId 查询
            List<SkuSaleAttrValue> saleAttrValues = skuSaleAttrValueMapper.selectList(skuIdWrapper);
            String collect = saleAttrValues.stream().map(skuSaleAttrValue -> {
                        return skuSaleAttrValue.getSaleAttrValueId().toString();
                    }).collect(Collectors.toList())
                    .stream().distinct().collect(Collectors.joining("|"));
            System.out.println("collect = " + collect);
            // 5、封装map结果
            resultMap.put(collect,skuId.toString());
        });
        return resultMap;
    }


}
