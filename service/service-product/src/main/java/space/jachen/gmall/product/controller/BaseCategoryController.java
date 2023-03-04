package space.jachen.gmall.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.product.entity.BaseCategory1;
import space.jachen.gmall.product.service.BaseCategoryService;

import java.util.List;

/**
 * 商品三级分类 前端控制器
 *
 * @author jachen
 * @since 2023-03-03
 */
@RestController
@RequestMapping("/admin/product")
public class BaseCategoryController {

    @Autowired
    private BaseCategoryService baseCategory1Service;

    @GetMapping("/getCategory1")
    public Result<List<BaseCategory1>> getCategory1(){
        List<BaseCategory1> category1List = baseCategory1Service.getCategory1();

        return Result.ok(category1List);
    }
}

