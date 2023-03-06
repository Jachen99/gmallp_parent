package space.jachen.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import space.jachen.gmall.domain.product.BaseCategoryTrademark;
import space.jachen.gmall.domain.product.BaseTrademark;
import space.jachen.gmall.product.mapper.BaseCategoryTrademarkMapper;
import space.jachen.gmall.product.mapper.BaseTrademarkMapper;
import space.jachen.gmall.product.service.BaseCategoryTrademarkService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 分类品牌 服务实现类
 *
 * @author jachen
 * @since 2023-03-06
 */
@Service
public class BaseCategoryTrademarkServiceImpl extends ServiceImpl<BaseCategoryTrademarkMapper, BaseCategoryTrademark> implements BaseCategoryTrademarkService {

    @Autowired
    private BaseTrademarkMapper baseTrademarkMapper;

    @Override
    public List<BaseTrademark> findTrademarkList(Long category3Id) {
        // 根据三级分类 id 关联查询出 BaseCategoryTrademark 对象
        List<BaseCategoryTrademark> baseCategoryTrademarks = baseMapper.selectList(
                new LambdaQueryWrapper<BaseCategoryTrademark>() {{
                    eq(BaseCategoryTrademark::getCategory3Id, category3Id);
                }}
        );
        if ( CollectionUtils.isEmpty(baseCategoryTrademarks) ){
            return null;
        }
        // 从 BaseCategoryTrademark 对象获取 BaseTrademark 的 ids 集合
        List<Long> ids = baseCategoryTrademarks.stream()
                .map(BaseCategoryTrademark::getTrademarkId)
                .collect(Collectors.toList());
        //  根据 ids 获取 BaseTrademark 列表
        return baseTrademarkMapper.selectBatchIds(ids);

    }

    @Override
    public List<BaseTrademark> findCurrentTrademarkList(Long category3Id) {
        // BaseCategoryTrademark 对象获取  与 findTrademarkList方法 相同
        List<BaseCategoryTrademark> baseCategoryTrademarks = baseMapper.selectList(
                new LambdaQueryWrapper<BaseCategoryTrademark>() {{
                    eq(BaseCategoryTrademark::getCategory3Id, category3Id);
                }}
        );
        if ( CollectionUtils.isEmpty(baseCategoryTrademarks) ){
            return baseTrademarkMapper.selectList(null);
        }
        // 排除 从 BaseCategoryTrademark 对象；并获取剩下 BaseTrademark对象 的 ids 集合
        List<Long> ids = baseCategoryTrademarks.stream()
                .map(BaseCategoryTrademark::getTrademarkId)
                .collect(Collectors.toList());
        return baseTrademarkMapper.selectList(null).stream()
                // 排除已有品牌id
                .filter(baseTrademark -> !ids.contains(baseTrademark.getId()))
                .collect(Collectors.toList());
    }
}
