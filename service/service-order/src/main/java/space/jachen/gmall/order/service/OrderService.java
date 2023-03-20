package space.jachen.gmall.order.service;

import space.jachen.gmall.domain.order.OrderInfo;

/**
 * @author JaChen
 * @date 2023/3/20 16:49
 */
public interface OrderService {

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
