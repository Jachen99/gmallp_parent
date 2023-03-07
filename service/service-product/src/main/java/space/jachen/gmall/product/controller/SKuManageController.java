package space.jachen.gmall.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.domain.product.SpuImage;
import space.jachen.gmall.product.service.BaseSpuService;

import java.util.List;

/**
 * 商品后台 - 商品信息管理
 *  -- sku 商品属性sku管理
 *
 * @author JaChen
 * @date 2023/3/7 9:40
 */
@RestController
@RequestMapping("/admin/product")
public class SKuManageController {

    @Autowired
    private BaseSpuService baseSpuService;

    /**
     * 根据spuId 获取spuImage 集合
     * @param spuId  spuId
     *
     * @return List<SpuImage>
     */
    @GetMapping("/spuImageList/{spuId}")
    public Result<List<SpuImage>> getSpuImageList(@PathVariable Long spuId){
        List<SpuImage> spuImageList = baseSpuService.getSpuImageList(spuId);

        return Result.ok(spuImageList);
    }


}
