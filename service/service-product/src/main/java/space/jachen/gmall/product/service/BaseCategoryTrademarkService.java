package space.jachen.gmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import space.jachen.gmall.domain.product.BaseCategoryTrademark;
import space.jachen.gmall.domain.product.BaseTrademark;

import java.util.List;

/**
 * 分类品牌 服务类
 *
 * @author jachen
 * @since 2023-03-06
 */
public interface BaseCategoryTrademarkService extends IService<BaseCategoryTrademark> {

    /**
     * 根据category3Id获取品牌列表
     * @param category3Id 三级分类的id
     *
     * @return List<BaseTrademark>
     */
    List<BaseTrademark> findTrademarkList(Long category3Id);
}
