package space.jachen.gmall.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.item.service.ItemService;

import java.util.Map;

/**
 * @author JaChen
 * @date 2023/3/8 10:07
 */
@RestController
@RequestMapping("/api/item")
public class ItemApiController {

    @Autowired
    private ItemService itemService;


    /**
     * 获取详情页数据
     * @param skuId  skuId
     * @return
     */
    @GetMapping("/{skuId}")
    public Result<Map<String,Object>> getSkuId(@PathVariable Long skuId){
        Map<String,Object> result = itemService.getSkuId(skuId);

        return Result.ok(result);
    }
}
