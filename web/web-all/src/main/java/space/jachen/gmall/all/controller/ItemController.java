package space.jachen.gmall.all.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.item.client.ItemFeignClient;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/3/9 15:52
 */
@Controller
public class ItemController {

    @Resource
    private ItemFeignClient itemFeignClient;

    /**
     * sku详情页面
     * @param skuId  skuId
     * @param model  Model
     * @return  String
     */
    @RequestMapping("{skuId}.html")
    public String getItem(@PathVariable Long skuId, Model model){
        // 通过skuId 查询skuInfo
        Result<Map<String,Object>> result = itemFeignClient.getSkuId(skuId);
        model.addAllAttributes(result.getData());

        return "item/index";
    }
}
