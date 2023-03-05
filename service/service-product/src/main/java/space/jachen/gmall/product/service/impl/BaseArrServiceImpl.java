package space.jachen.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import space.jachen.gmall.common.execption.GmallException;
import space.jachen.gmall.common.result.ResultCodeEnum;
import space.jachen.gmall.domain.product.BaseAttrInfo;
import space.jachen.gmall.domain.product.BaseAttrValue;
import space.jachen.gmall.product.mapper.BaseAttrInfoMapper;
import space.jachen.gmall.product.mapper.BaseAttrValueMapper;
import space.jachen.gmall.product.service.BaseAttrService;

import java.util.List;

/**
 * 平台属性接口实现类
 *
 * @author JaChen
 * @date 2023/3/4 16:32
 */
@Service
@SuppressWarnings("all")
@Transactional(rollbackFor = Exception.class)
public class BaseArrServiceImpl implements BaseAttrService {

    @Autowired
    private BaseAttrInfoMapper baseAttrInfoMapper;
    @Autowired
    private BaseAttrValueMapper baseAttrValueMapper;

    @Override
    public List<BaseAttrInfo> attrInfoList(Long category1Id, Long category2Id, Long category3Id) {

        // 返回三级级联查询的响应数据-平台属性
        return baseAttrInfoMapper.attrInfoList(category1Id,category2Id,category3Id);
    }

    @Override
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        // 处理 平台属性
        if (null != baseAttrInfo.getId()){
            baseAttrInfoMapper.updateById(baseAttrInfo);
        }else {
            baseAttrInfoMapper.insert(baseAttrInfo);
        }
        // 处理平台属性值  先删除脏数据再插入更新
        baseAttrValueMapper.delete(
                new LambdaQueryWrapper<BaseAttrValue>(){{
                    eq(BaseAttrValue::getAttrId,baseAttrInfo.getId());
                }}
        );
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        if ( !StringUtils.isEmpty(attrValueList) && attrValueList.size() > 0 ){
            for (BaseAttrValue baseAttrValue : attrValueList) {
                baseAttrValue.setAttrId(baseAttrInfo.getId());

                baseAttrValueMapper.insert(baseAttrValue);
            }
        }
    }

    @Override
    public List<BaseAttrValue> getAttrValueList(Long attrId) {
        // 对于并发情况，我们要先对BaseAttrInfo进行查询，确保attrInfo含有对应的getAttrValueList，避免查到脏数据。
        BaseAttrInfo attrInfo = baseAttrInfoMapper.selectById(attrId);
        if (null == attrInfo.getAttrValueList()){
            new GmallException(ResultCodeEnum.FAIL);
        }

        // 查询并返回BaseAttrValue的list
        return baseAttrValueMapper.selectList(
                new LambdaQueryWrapper<BaseAttrValue>(){{
                    eq(BaseAttrValue::getAttrId,attrId);
                }}
        );
    }
}
