package space.jachen.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import space.jachen.gmall.domain.product.BaseCategory3;
import space.jachen.gmall.product.mapper.BaseCategoryTestMapper;
import org.springframework.stereotype.Service;
import space.jachen.gmall.product.service.BaseCategoryTestService;

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
@SuppressWarnings("all")
public class BaseCategoryTestServiceImpl implements BaseCategoryTestService {

    @Autowired
    private BaseCategoryTestMapper baseCategoryTestMapper;

    @Override
    public List<BaseCategory3> findBaseCategory3ByCategory3IdList() {

        BaseCategory3 baseCategory3 = new BaseCategory3();
        baseCategory3.setId(160L);

        return baseCategoryTestMapper.selectList(
                new LambdaQueryWrapper<BaseCategory3>(){{
                    eq(BaseCategory3::getId,baseCategory3.getId());
                }}
        );

    }
}
