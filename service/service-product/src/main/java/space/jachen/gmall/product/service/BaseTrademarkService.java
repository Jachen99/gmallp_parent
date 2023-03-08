package space.jachen.gmall.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import space.jachen.gmall.domain.product.BaseTrademark;

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

    /**
     * 根据品牌Id 获取品牌数据
     * @param tmId 品牌Id
     * @return  BaseTrademark
     */
    BaseTrademark getTrademark(Long tmId);

}
