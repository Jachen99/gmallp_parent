package space.jachen.gmall.order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import space.jachen.gmall.domain.enums.ProcessStatus;
import space.jachen.gmall.domain.order.OrderInfo;

import java.util.List;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/3/20 16:49
 */
public interface OrderService extends IService<OrderInfo> {


    /**
     * 更新过期订单
     * @param orderId
     * @param flag
     */
    void execExpiredOrder(Long orderId,String flag);



    /**
     * 拆单接口
     * @param orderId
     * @param wareSkuMap
     * @return
     */
    List<OrderInfo> orderSplit(Long orderId, String wareSkuMap);

    /**
     * 发送消息给库存
     * @param orderId
     */
    void sendOrderStatus(Long orderId);


    /**
     *将orderInfo变为map集合
     * @param orderInfo
     */
     Map initWareOrder(OrderInfo orderInfo);



    /**
     * 根据订单Id 查询订单信息
     * @param orderId
     * @return
     */
    OrderInfo getOrderInfo(Long orderId);


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
    Long saveOrderInfo(OrderInfo orderInfo,ProcessStatus processStatus);

}
