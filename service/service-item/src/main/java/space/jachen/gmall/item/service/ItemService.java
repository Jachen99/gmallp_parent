package space.jachen.gmall.item.service;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/3/8 10:09
 */
public interface ItemService {


    /**
     * 获取详情页数据
     * @param skuId  skuId
     * @return  Map<String,Object>
     */
    Map<String, Object> getSkuId(Long skuId);
}
