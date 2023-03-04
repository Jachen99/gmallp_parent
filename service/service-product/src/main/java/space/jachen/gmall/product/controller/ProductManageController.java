package space.jachen.gmall.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.domain.product.BaseCategory1;
import space.jachen.gmall.domain.product.BaseCategory2;
import space.jachen.gmall.domain.product.BaseCategory3;
import space.jachen.gmall.product.service.BaseCategoryService;

import java.util.List;

/**
 * 商品管理
 *
 * @author jachen
 * @since 2023-03-03
 */
@RestController
@RequestMapping("/admin/product")
public class ProductManageController {

    @Autowired
    private BaseCategoryService baseCategory1Service;

    /**
     * /admin/product/getCategory3/{category2Id}
     * @param category2Id  二级分类id
     * 获取三级分类数据
     *
     * @return  List<BaseCategory3>
     */
    @GetMapping("getCategory3/{category2Id}")
    public Result<List<BaseCategory3>> getCategory3(@PathVariable Long category2Id){
        List<BaseCategory3> category3List = baseCategory1Service.getCategory3(category2Id);

        return Result.ok(category3List);
    }


    /**
     * /admin/product/getCategory2/{category1Id}
     * @param category1Id  一级分类id
     * 根据一级id获取二级分类数据
     *
     * @return  List<BaseCategory2>
     */
    @GetMapping("/getCategory2/{category1Id}")
    public Result<List<BaseCategory2>> getCategory2(@PathVariable Long category1Id){
        List<BaseCategory2> category2List = baseCategory1Service.getCategory2(category1Id);

        return Result.ok(category2List);
    }


    /**
     * /admin/product/getCategory1
     * 获取一级分类数据
     *
     * @return  List<BaseCategory1>
     */
    @GetMapping("/getCategory1")
    public Result<List<BaseCategory1>> getCategory1(){
        List<BaseCategory1> category1List = baseCategory1Service.getCategory1();

        return Result.ok(category1List);
    }
}

