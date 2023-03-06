package space.jachen.gmall.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.domain.product.SpuInfo;
import space.jachen.gmall.product.service.BaseSpuService;

/**
 * 商品后台 - 商品信息管理
 *  -- spu 商品属性spu管理
 *  -- sku 商品属性sku管理
 *
 * @author JaChen
 * @date 2023/3/6 9:20
 */
@RestController
@RequestMapping("/admin/product")
public class ProductManageController {

    @Autowired
    private BaseSpuService baseSpuService;

    /**
     * spu分页列表
     * @param page  当前页
     * @param limit  每页个数
     * @param spuInfo  查询条件对象 category3Id
     *
     * @return  Page<SpuInfo>
     */
    @GetMapping("/{page}/{limit}")
    public Result<IPage<SpuInfo>> getSpuPageList(@PathVariable Long page, @PathVariable Long limit, SpuInfo spuInfo){
        // 封装分页对象
        IPage<SpuInfo> spuPage = new Page<>(page,limit);
        IPage<SpuInfo> spuInfoPage = baseSpuService.getSpuPageList(spuPage,spuInfo);

        return Result.ok(spuInfoPage);
    }

}
