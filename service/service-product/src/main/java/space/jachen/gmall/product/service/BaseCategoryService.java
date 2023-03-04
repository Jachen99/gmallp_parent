package space.jachen.gmall.product.service;

import space.jachen.gmall.domain.product.BaseCategory1;

import java.util.List;

/**
 * <p>
 * 一级分类表 服务类
 * </p>
 *
 * @author jachen
 * @since 2023-03-03
 */
public interface BaseCategoryService {

    /**
     * 获取一级分类数据
     * @return  List<BaseCategory1>
     */
    List<BaseCategory1> getCategory1();
}