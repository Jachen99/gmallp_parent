package space.jachen.gmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import space.jachen.gmall.domain.product.BaseCategoryTrademark;
import space.jachen.gmall.domain.product.BaseTrademark;
import space.jachen.gmall.domain.product.CategoryTrademarkVo;

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

    /**
     * 根据category3Id获取可选品牌列表
     * @param category3Id  三级分类id
     *
     * @return List<BaseTrademark>
     */
    List<BaseTrademark> findCurrentTrademarkList(Long category3Id);

    /**
     * 保存分类品牌关联
     * @param categoryTrademarkVo  三级分类编号和品牌id集合
     */
    void saveTrademark(CategoryTrademarkVo categoryTrademarkVo);

    /**
     * 删除分类品牌关联
     * @param category3Id  三级分类id
     * @param trademarkId  品牌id
     */
    void removeTrademark(Long category3Id, Long trademarkId);
}
