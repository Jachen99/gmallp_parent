package space.jachen.gmall.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import space.jachen.gmall.domain.enums.ProcessStatus;
import space.jachen.gmall.domain.order.OrderInfo;

/**
 * @author JaChen
 * @date 2023/3/20 16:49
 */
public interface OrderService {


    /**
     * 处理过期订单
     * @param orderId
     */
    void execExpiredOrder(Long orderId);


    /**
     * 根据订单Id 修改订单的状态
     * @param orderId
     * @param processStatus
     */
    void updateOrderStatus(Long orderId, ProcessStatus processStatus);


    /**
     * 分页展示订单列表
     * @param pageParam  pageParam
     * @param userId  userId
     * @return  IPage<OrderInfo>
     */
    IPage<OrderInfo> getPage(Page<OrderInfo> pageParam, String userId);

    /**
     * 验证库存
     * @param skuId
     * @param skuNum
     * @return
     */
    boolean checkStock(Long skuId, Integer skuNum);


    /**
     * 生产流水号
     * @param userId  userId
     * @return String
     */
    String getTradeNo(String userId);

    /**
     * 比较流水号
     * @param userId 获取缓存中的流水号
     * @param tradeCodeNo  页面传递过来的流水号
     * @return  boolean
     */
    boolean checkTradeCode(String userId, String tradeCodeNo);


    /**
     * 删除流水号
     * @param userId  userId
     */
    void deleteTradeNo(String userId);


    /**
     * 保存订单
     * @param orderInfo  OrderInfo
     * @return Long
     */
    Long saveOrderInfo(OrderInfo orderInfo);
}
