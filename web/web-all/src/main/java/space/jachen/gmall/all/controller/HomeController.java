package space.jachen.gmall.all.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.product.client.ProductFeignClient;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * @author JaChen
 * @date 2023/3/13 18:38
 */
@Controller
@SuppressWarnings("all")
public class HomeController {

    @Autowired
    private ProductFeignClient productFeignClient;
    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping("/api/list/createIndex")
    public Result createIndex(){

        List<JSONObject> data = productFeignClient.getBaseCategoryList().getData();
        Context context = new Context();
        // 设置数据上下文对象
        context.setVariable("list",data);

        FileWriter fileWriter = null;
        try {
            // 设置输出路径
             fileWriter = new FileWriter("G:\\mywork\\index.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        templateEngine.process("index/index",context,fileWriter);

        return Result.ok();
    }

}
