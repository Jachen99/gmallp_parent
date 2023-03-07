package space.jachen.gmall.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import space.jachen.gmall.domain.product.SkuImage;
import space.jachen.gmall.domain.product.SkuInfo;
import space.jachen.gmall.product.mapper.SkuImageMapper;
import space.jachen.gmall.product.mapper.SkuInfoMapper;
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

    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        // 保存基本信息
        skuInfoMapper.insert(skuInfo);
        // 保存图片信息
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        if ( !CollectionUtils.isEmpty(skuImageList)){
            skuImageList.forEach(skuImage -> {
                // 设置 skuId 字段
                skuImage.setSkuId(skuInfo.getId());
                // 存入数据库
                skuImageMapper.insert(skuImage);
            });
        // 保存
        }
    }
}
