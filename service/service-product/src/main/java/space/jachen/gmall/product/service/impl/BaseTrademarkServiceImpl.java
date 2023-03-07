package space.jachen.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.jachen.gmall.domain.product.BaseCategory3;
import space.jachen.gmall.domain.product.BaseTrademark;
import space.jachen.gmall.domain.product.CategoryTrademarkVo;
import space.jachen.gmall.product.mapper.BaseCategory3Mapper;
import space.jachen.gmall.product.mapper.BaseTrademarkMapper;
import space.jachen.gmall.product.service.BaseTrademarkService;

import java.util.List;

/**
 * 品牌表 服务实现类
 *
 * @author jachen
 * @since 2023-03-06
 */
@Service
public class BaseTrademarkServiceImpl extends ServiceImpl<BaseTrademarkMapper, BaseTrademark> implements BaseTrademarkService {

    @Autowired
    private BaseCategory3Mapper baseCategory3Mapper;

    @Override
    public IPage<BaseTrademark> getBaseTrademarkPage(IPage<BaseTrademark> baseTrademarkIPage) {
        return baseMapper.selectPage(baseTrademarkIPage,null);
    }

    @Override
    public BaseTrademark getTrademark(Long tmId) {
        return baseMapper.selectOne(
                new LambdaQueryWrapper<BaseTrademark>(){{
                    eq(BaseTrademark::getId,tmId);
                }}
        );
    }

    @Override
    public List<BaseCategory3> findBaseCategory3ByCategory3IdList(CategoryTrademarkVo categoryTrademarkVo) {
        if ( null != categoryTrademarkVo){
            // 查询idList
            List<Long> idList = categoryTrademarkVo.getTrademarkIdList();

            return baseCategory3Mapper.selectBatchIds(idList);
        }
        return null;
    }

}
