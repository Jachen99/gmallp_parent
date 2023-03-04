package space.jachen.gmall.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import space.jachen.gmall.domain.product.BaseCategory1;
import space.jachen.gmall.domain.product.BaseCategory2;
import space.jachen.gmall.domain.product.BaseCategory3;

import java.util.List;

/**
 * <p>
 * 分类表 Mapper 接口
 * </p>
 *
 * @author jachen
 * @since 2023-03-03
 */
@Mapper
public interface BaseCategoryMapper {

    @Select("select id,name from base_category1")
    List<BaseCategory1> getCategory1();

    @Select("select id,name from base_category2 where category1_id = #{category1Id}")
    List<BaseCategory2> getCategory2(@Param("category1Id") Long category1Id);

    @Select("select id,name from base_category3 where category2_id = #{category2Id}")
    List<BaseCategory3> getCategory3(@Param("category2Id") Long category2Id);
}
