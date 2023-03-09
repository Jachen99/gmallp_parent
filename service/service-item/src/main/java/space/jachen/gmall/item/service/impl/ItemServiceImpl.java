package space.jachen.gmall.item.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.jachen.gmall.item.service.ItemService;
import space.jachen.gmall.product.client.ProductFeignClient;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/3/8 10:10
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ProductFeignClient productFeignClient;



    @Override
    public Map<String, Object> getSkuId(Long skuId) {
        return null;
    }
}
