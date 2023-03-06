package space.jachen.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.jachen.gmall.domain.product.SpuInfo;
import space.jachen.gmall.product.mapper.SpuInfoMapper;
import space.jachen.gmall.product.service.BaseSpuService;

/**
 * @author JaChen
 * @date 2023/3/6 9:56
 */
@Service
public class BaseSpuServiceImpl implements BaseSpuService {

    @Autowired
    private SpuInfoMapper spuInfoMapper;

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
}
