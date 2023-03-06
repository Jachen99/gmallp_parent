package space.jachen.gmall.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import space.jachen.gmall.domain.product.SpuInfo;

/**
 * @author JaChen
 * @date 2023/3/6 9:52
 */
public interface BaseSpuService {
    /**
     * 获取spu分页列表
     * @param spuPage  分页对象
     * @param spuInfo  查询条件对象 category3Id
     *
     * @return   Page<SpuInfo>
     */
    IPage<SpuInfo> getSpuPageList(IPage<SpuInfo> spuPage, SpuInfo spuInfo);
}
