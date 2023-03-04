package space.jachen.gmall.product.service;

import space.jachen.gmall.domain.product.BaseAttrInfo;

import java.util.List;

/**
 * 平台属性接口
 *
 * @author JaChen
 * @date 2023/3/4 16:29
 */
public interface BaseAttrService {

    /**
     * 根据分类Id 获取平台属性集合
     * @param category1Id 一级分类id
     * @param category2Id 二级分类id
     * @param category3Id 三级分类id
     *
     * @return List<BaseAttrInfo>
     */
    List<BaseAttrInfo> attrInfoList(Long category1Id, Long category2Id, Long category3Id);
}
