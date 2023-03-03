package space.jachen.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import space.jachen.gmall.product.entity.BaseCategory3;
import space.jachen.gmall.product.mapper.BaseCategory3Mapper;
import space.jachen.gmall.product.service.BaseCategory3Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 三级分类表 服务实现类
 * </p>
 *
 * @author jachen
 * @since 2023-03-03
 */
@Service
public class BaseCategory3ServiceImpl extends ServiceImpl<BaseCategory3Mapper, BaseCategory3> implements BaseCategory3Service {

    @Override
    public List<BaseCategory3> findBaseCategory3ByCategory3IdList() {

        BaseCategory3 baseCategory3 = new BaseCategory3();
        baseCategory3.setId(160L);

        return baseMapper.selectList(
                new LambdaQueryWrapper<BaseCategory3>(){{
                    eq(BaseCategory3::getId,baseCategory3.getId());
                }}
        );

    }
}
