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

    IPage<BaseTrademark> getBaseTrademarkPage(IPage<BaseTrademark> baseTrademarkIPage);

}
