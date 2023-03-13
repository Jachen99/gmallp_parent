package space.jachen.gmall.all.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.product.client.ProductFeignClient;

/**
 * @author JaChen
 * @date 2023/3/13 18:38
 */
@Controller
@SuppressWarnings("all")
public class HomeController {

    @Autowired
    private ProductFeignClient productFeignClient;

    @GetMapping("/api/list/createIndex")
    public Result createIndex(){

        return Result.ok();
    }

}
