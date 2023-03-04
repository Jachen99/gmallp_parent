package space.jachen.gmall.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import space.jachen.gmall.domain.product.BaseCategory1;
import space.jachen.gmall.domain.product.BaseCategory2;
import space.jachen.gmall.domain.product.BaseCategory3;
import space.jachen.gmall.product.mapper.BaseCategoryMapper;
import org.springframework.stereotype.Service;
import space.jachen.gmall.product.service.BaseCategoryService;

import java.util.List;

/**
 * <p>
 * 一级分类表 服务实现类
 * </p>
 *
 * @author jachen
 * @since 2023-03-03
 */
@Service
public class BaseCategoryServiceImpl implements BaseCategoryService {

    @Autowired
    private BaseCategoryMapper baseCategoryMapper;

    @Override
    public List<BaseCategory1> getCategory1() {
        return baseCategoryMapper.getCategory1();
    }

    @Override
    public List<BaseCategory2> getCategory2(Long category1Id) {
        return baseCategoryMapper.getCategory2(category1Id);
    }

    @Override
    public List<BaseCategory3> getCategory3(Long category2Id) {
        return baseCategoryMapper.getCategory3(category2Id);
    }
}
