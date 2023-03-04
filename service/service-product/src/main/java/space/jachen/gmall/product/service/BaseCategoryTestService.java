package space.jachen.gmall.product.service;

import space.jachen.gmall.domain.product.BaseCategory3;

import java.util.List;

/**
 * <p>
 * 三级分类表 服务类
 * </p>
 *
 * @author jachen
 * @since 2023-03-03
 */
public interface BaseCategoryTestService{


    /**
     * 根据三级分类Id集合查询
     *
     * @return  List<BaseCategory3>
     */
    List<BaseCategory3> findBaseCategory3ByCategory3IdList();
}
