package space.jachen.gmall.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import space.jachen.gmall.domain.product.BaseCategory1;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 一级分类表 Mapper 接口
 * </p>
 *
 * @author jachen
 * @since 2023-03-03
 */
@Mapper
public interface BaseCategoryMapper extends BaseMapper<BaseCategory1> {

    @Select("select * from base_category1")
    List<BaseCategory1> getCategory1();
}
