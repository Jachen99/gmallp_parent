package space.jachen.gmall.product.service;

import space.jachen.gmall.domain.product.BaseAttrInfo;
import space.jachen.gmall.domain.product.BaseCategory1;
import space.jachen.gmall.domain.product.BaseCategory2;
import space.jachen.gmall.domain.product.BaseCategory3;

import java.util.List;

/**
 * 平台分类表 服务类
 *
 * @author jachen
 * @since 2023-03-03
 */
public interface BaseCategoryService {

    /**
     * 获取一级分类数据
     *
     * @return  List<BaseCategory1>
     */
    List<BaseCategory1> getCategory1();

    /**
     * @param category1Id  一级分类id
     * 根据一级id获取二级分类数据
     *
     * @return  List<BaseCategory2>
     */
    List<BaseCategory2> getCategory2(Long category1Id);

    /**
     * @param category2Id  二级分类id
     * 根据二级id获取三级分类数据
     *
     * @return  List<BaseCategory3>
     */
    List<BaseCategory3> getCategory3(Long category2Id);

}
