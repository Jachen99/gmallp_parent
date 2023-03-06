package space.jachen.gmall.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("/admin/product")
public class BaseTradeMarkController {
    @Autowired
    private BaseTrademarkService baseTrademarkService;



    /**
     * 品牌分页列表
     * @param page  当前页
     * @param limit  每页个数
     * @return IPage<BaseTrademark>
     */
    @GetMapping("/baseTrademark/{page}/{limit}")
    public Result<IPage<BaseTrademark>> getBaseTrademarkPage(@PathVariable Long page, @PathVariable Long limit){
        // 封装分页条件
        IPage<BaseTrademark> baseTrademarkIPage = new Page<>(page,limit);
        baseTrademarkIPage = baseTrademarkService.getBaseTrademarkPage(baseTrademarkIPage);

        return Result.ok(baseTrademarkIPage);
    }

}
