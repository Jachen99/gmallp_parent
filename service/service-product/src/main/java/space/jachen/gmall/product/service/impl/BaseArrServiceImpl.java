package space.jachen.gmall.product.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.jachen.gmall.domain.product.BaseAttrInfo;
import space.jachen.gmall.product.mapper.BaseAttrMapper;
import space.jachen.gmall.product.service.BaseAttrService;

import java.util.List;

/**
 * 平台属性接口实现类
 *
 * @author JaChen
 * @date 2023/3/4 16:32
 */
@Service
public class BaseArrServiceImpl implements BaseAttrService {

    @Autowired
    private BaseAttrMapper baseAttrMapper;

    @Override
    public List<BaseAttrInfo> attrInfoList(Long category1Id, Long category2Id, Long category3Id) {

        return baseAttrMapper.attrInfoList(category1Id,category2Id,category3Id);
    }
}
