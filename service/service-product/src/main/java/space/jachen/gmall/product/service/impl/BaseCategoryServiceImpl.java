package space.jachen.gmall.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import space.jachen.gmall.product.entity.BaseCategory1;
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
}
