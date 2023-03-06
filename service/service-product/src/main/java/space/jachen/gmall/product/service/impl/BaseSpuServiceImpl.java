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
/*            spuImageList.stream().map(spuImage -> {
                spuImage.setSpuId(spuInfo.getId());
                return spuImageMapper.insert(spuImage);
            });*/
            for (SpuImage spuImage : spuImageList) {
                spuImage.setSpuId(spuInfo.getId());

                spuImageMapper.insert(spuImage);
            }
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
}
