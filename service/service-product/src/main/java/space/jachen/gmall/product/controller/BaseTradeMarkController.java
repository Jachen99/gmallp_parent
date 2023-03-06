package space.jachen.gmall.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.domain.product.BaseTrademark;
import space.jachen.gmall.product.service.BaseTrademarkService;

/**
 * 商品后台 - 基本信息管理
 *   -- 品牌列表
 *
 * @author JaChen
 * @date 2023/3/6 12:54
 */
@RestController
@RequestMapping("/admin/product/baseTrademark")
public class BaseTradeMarkController {
    @Autowired
    private BaseTrademarkService baseTrademarkService;


    /**
     * 删除品牌对象
     * @param id  品牌Id
     *
     * @return Result<Object>
     */
    @DeleteMapping("/remove/{id}")
    public Result<Object> removeBaseTrademarkById(@PathVariable Long id){
        baseTrademarkService.getBaseMapper().deleteById(id);

        return Result.ok();
    }


    /**
     * 获取品牌对象
     * @param id  品牌Id
     *
     * @return  BaseTrademark 品牌对象
     */
    @GetMapping("/get/{id}")
    public Result<BaseTrademark> getBaseTrademarkById(@PathVariable Long id){
        BaseTrademark baseTrademark = baseTrademarkService.getBaseMapper().selectById(id);

        return Result.ok(baseTrademark);
    }


    /**
     * 修改更新品牌对象
     * @param baseTrademark  品牌对象
     * @return Result<Object>
     */
    @PutMapping("/update")
    public Result<Object> updateBaseTradeMark(@RequestBody BaseTrademark baseTrademark){
        baseTrademarkService.updateById(baseTrademark);

        return Result.ok();
    }


    /**
     * 新增品牌对象
     * @param baseTrademark  品牌对象
     *
     * @return Result<Object>
     */
    @PostMapping("/save")
    public Result<Object> insertBaseTradeMark(@RequestBody BaseTrademark baseTrademark){
        baseTrademarkService.save(baseTrademark);

        return Result.ok();
    }


    /**
     * 品牌分页列表
     * @param page  当前页
     * @param limit  每页个数
     * @return IPage<BaseTrademark>
     */
    @GetMapping("/{page}/{limit}")
    public Result<IPage<BaseTrademark>> getBaseTrademarkPage(@PathVariable Long page, @PathVariable Long limit){
        // 封装分页条件
        IPage<BaseTrademark> baseTrademarkIPage = new Page<>(page,limit);
        baseTrademarkIPage = baseTrademarkService.getBaseTrademarkPage(baseTrademarkIPage);

        return Result.ok(baseTrademarkIPage);
    }

}
