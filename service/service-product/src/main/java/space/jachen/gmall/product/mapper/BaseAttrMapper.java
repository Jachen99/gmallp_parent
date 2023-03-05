package space.jachen.gmall.product.mapper;

import org.apache.ibatis.annotations.*;
import space.jachen.gmall.domain.product.BaseAttrInfo;
import space.jachen.gmall.domain.product.BaseAttrValue;

import java.util.List;

/**
 * @author JaChen
 * @date 2023/3/4 16:34
 */
@Mapper
public interface BaseAttrMapper {

    List<BaseAttrInfo> attrInfoList(@Param("category1Id") Long category1Id, @Param("category2Id") Long category2Id, @Param("category3Id") Long category3Id);
}
