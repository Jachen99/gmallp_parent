package space.jachen.gmall.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import space.jachen.gmall.domain.product.BaseCategory3;
import space.jachen.gmall.domain.product.BaseTrademark;
import space.jachen.gmall.domain.product.CategoryTrademarkVo;

import java.util.List;

/**
 * 品牌表 服务类
 *
 * @author jachen
 * @since 2023-03-06
 */
public interface BaseTrademarkService extends IService<BaseTrademark> {

    /**
     * 品牌分页列表
     * @param baseTrademarkIPage  IPage 对象
     *
     * @return IPage<BaseTrademark>
     */
    IPage<BaseTrademark> getBaseTrademarkPage(IPage<BaseTrademark> baseTrademarkIPage);

    BaseTrademark getTrademark(Long tmId);

    /**
     * 前台api
     *      - 根据三级分类Id集合 查询
     * @param categoryTrademarkVo  三级分类编号和品牌id集合 View
     * @return List<BaseCategory3>
     */
    List<BaseCategory3> findBaseCategory3ByCategory3IdList(CategoryTrademarkVo categoryTrademarkVo);
}
