package space.jachen.gmall.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import space.jachen.gmall.common.config.DeadLetterMqConfig;
import space.jachen.gmall.common.constant.MqConst;
import space.jachen.gmall.common.constant.RedisConst;
import space.jachen.gmall.common.service.RabbitService;
import space.jachen.gmall.domain.enums.OrderStatus;
import space.jachen.gmall.domain.enums.ProcessStatus;
import space.jachen.gmall.domain.order.OrderDetail;
import space.jachen.gmall.domain.order.OrderInfo;
import space.jachen.gmall.order.mapper.OrderDetailMapper;
import space.jachen.gmall.order.mapper.OrderInfoMapper;
import space.jachen.gmall.order.service.OrderService;

import java.util.*;

/**
 * @author JaChen
 * @date 2023/3/20 16:50
 */
@Service
@SuppressWarnings("all")
public class OrderServiceImpl extends ServiceImpl<OrderInfoMapper,OrderInfo> implements OrderService {

    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitService rabbitService;
    @Value("${ware.url}")
    private String WARE_URL;

    @Override
    public void execExpiredOrder(Long orderId, String flag) {
        // 调用方法 状态
        updateOrderStatus(orderId,ProcessStatus.CLOSED);
        if ("2".equals(flag)){
            // 给支付服务发送消息队列，关闭支付宝的交易记录。
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_PAYMENT_CLOSE,MqConst.ROUTING_PAYMENT_CLOSE,orderId);
        }
    }

    @Override
    @Transactional
    public List<OrderInfo> orderSplit(Long orderId, String wareSkuMap) {
        List<OrderInfo> orderInfoArrayList = new ArrayList<>();
        /*
        1.  先获取到原始订单 107
        2.  将wareSkuMap 转换为我们能操作的对象 [{"wareId":"1","skuIds":["2","10"]},{"wareId":"2","skuIds":["3"]}]
            方案一：class Param{
                        private String wareId;
                        private List<String> skuIds;
                    }
            方案二：看做一个Map mpa.put("wareId",value); map.put("skuIds",value)

        3.  创建一个新的子订单 108 109 。。。
        4.  给子订单赋值
        5.  保存子订单到数据库
        6.  修改原始订单的状态
        7.  测试
         */
        OrderInfo orderInfoOrigin = getOrderInfo(orderId);
        // 获取数据
        List<Map> maps = JSON.parseArray(wareSkuMap, Map.class);
        if (maps != null) {
            for (Map map : maps) {
                String wareId = (String) map.get("wareId");
                List<String> skuIds = (List<String>) map.get("skuIds");
                OrderInfo subOrderInfo = new OrderInfo();
                // 属性拷贝
                BeanUtils.copyProperties(orderInfoOrigin, subOrderInfo);
                // 防止主键冲突
                subOrderInfo.setId(null);
                subOrderInfo.setParentOrderId(orderId);
                // 赋值仓库Id
                subOrderInfo.setWareId(wareId);

                // 计算子订单的金额: 必须有订单明细
                // 获取到子订单明细
                // 声明一个集合来存储子订单明细
                List<OrderDetail> orderDetails = new ArrayList<>();

                List<OrderDetail> orderDetailList = orderInfoOrigin.getOrderDetailList();
                // 表示主主订单明细中获取到子订单的明细
                if (orderDetailList != null && orderDetailList.size() > 0) {
                    for (OrderDetail orderDetail : orderDetailList) {
                        // 获取子订单明细的商品Id
                        for (String skuId : skuIds) {
                            if (Long.parseLong(skuId) == orderDetail.getSkuId().longValue()) {
                                // 将订单明细添加到集合
                                orderDetails.add(orderDetail);
                            }
                        }
                    }
                }
                subOrderInfo.setOrderDetailList(orderDetails);
                // 计算总金额
                subOrderInfo.sumTotalAmount();
                // 保存子订单
                saveOrderInfo(subOrderInfo,ProcessStatus.SPLIT);
                // 将子订单添加到集合中
                orderInfoArrayList.add(subOrderInfo);
            }
        }
        // 修改原始订单的状态  已经拆单
        updateOrderStatus(orderId, ProcessStatus.SPLIT);
        return orderInfoArrayList;
    }


    @Override
    public void sendOrderStatus(Long orderId) {
        // 更新订单状态为 已通知仓储
        this.updateOrderStatus(orderId, ProcessStatus.NOTIFIED_WARE);
        String wareJson = initWareOrder(orderId);

        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_WARE_STOCK, MqConst.ROUTING_WARE_STOCK, wareJson);
    }

    // 根据orderId 获取json 字符串
    private String initWareOrder(Long orderId) {
        // 通过orderId 获取orderInfo
        OrderInfo orderInfo = getOrderInfo(orderId);
        // 将orderInfo中部分数据转换为Map
        Map map = initWareOrder(orderInfo);

        return JSON.toJSONString(map);
    }


    @Override
    public Map initWareOrder(OrderInfo orderInfo) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("orderId", orderInfo.getId());
        map.put("consignee", orderInfo.getConsignee());
        map.put("consigneeTel", orderInfo.getConsigneeTel());
        map.put("orderComment", orderInfo.getOrderComment());
        map.put("orderBody", orderInfo.getTradeBody());
        map.put("deliveryAddress", orderInfo.getDeliveryAddress());
        map.put("paymentWay", "2");  // 2:在线支付
        map.put("wareId", "");// 仓库Id ，减库存拆单时需要使用
        /*
        details:[{skuId:101,skuNum:1,skuName:
        ’小米手64G’},
        {skuId:201,skuNum:1,skuName:’索尼耳机’}]
         */
        List<Map> mapArrayList = new ArrayList<>();
        List<OrderDetail> orderDetailList = orderInfo.getOrderDetailList();
        for (OrderDetail orderDetail : orderDetailList) {
            HashMap<String, Object> orderDetailMap = new HashMap<>();
            orderDetailMap.put("skuId", orderDetail.getSkuId());
            orderDetailMap.put("skuNum", orderDetail.getSkuNum());
            orderDetailMap.put("skuName", orderDetail.getSkuName());
            mapArrayList.add(orderDetailMap);
        }
        map.put("details", mapArrayList);
        return map;

    }

    @Override
    public OrderInfo getOrderInfo(Long orderId) {
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        List<OrderDetail> orderDetailList = orderDetailMapper.selectList(queryWrapper);
        orderInfo.setOrderDetailList(orderDetailList);
        return orderInfo;
    }

    @Override
    public void execExpiredOrder(Long orderId) {
        // orderInfo
        updateOrderStatus(orderId, ProcessStatus.CLOSED);
        // 发送消息 关闭交易
        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_PAYMENT_CLOSE, MqConst.ROUTING_PAYMENT_CLOSE, orderId);
    }

    @Override
    public void updateOrderStatus(Long orderId, ProcessStatus processStatus) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(orderId);
        orderInfo.setProcessStatus(processStatus.name());
        orderInfo.setOrderStatus(processStatus.getOrderStatus().name());

        orderInfoMapper.updateById(orderInfo);
    }

    @Override
    public IPage<OrderInfo> getPage(Page<OrderInfo> pageParam, String userId) {
        IPage<OrderInfo> page = orderInfoMapper.selectPageByUserId(pageParam, userId);
        page.getRecords().stream().forEach(item -> {
            item.setOrderStatusName(OrderStatus.getStatusNameByStatus(item.getOrderStatus()));
        });

        return page;
    }

    @Override
    public boolean checkStock(Long skuId, Integer skuNum) {
        String result = com.atguigu.gmall.common.util.HttpClientUtil
                .doGet(WARE_URL + "/hasStock?skuId=" + skuId + "&num=" + skuNum);
        return "1".equals(result);
    }

    @Override
    public String getTradeNo(String userId) {
        String tradeKey = getTradeKey(userId);
        // 设置流水号
        String tradeNo = String.valueOf(UUID.randomUUID());
        redisTemplate.opsForValue().set(tradeKey, tradeNo);

        return tradeNo;
    }

    @Override
    public boolean checkTradeCode(String userId, String tradeCodeNo) {
        String tradeKey = getTradeKey(userId);
        String redisTradeNo = redisTemplate.opsForValue().get(tradeKey);
        if (!StringUtils.isEmpty(redisTradeNo)) {
            if (redisTradeNo.equals(tradeCodeNo))
                return true;
        }
        return false;
    }

    @Override
    public void deleteTradeNo(String userId) {
        String tradeKey = getTradeKey(userId);
        if (!StringUtils.isEmpty(tradeKey))
            redisTemplate.delete(tradeKey);
    }

    @Override
    @Transactional
    public Long saveOrderInfo(OrderInfo orderInfo,ProcessStatus processStatus) {
        orderInfo.sumTotalAmount();
        orderInfo.setOrderStatus(processStatus.getOrderStatus().name());
        orderInfo.setCreateTime(new Date());
        orderInfo.setOutTradeNo("gmall" + UUID.randomUUID());
        // 失效时间
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 1);
        orderInfo.setExpireTime(instance.getTime());
        // 进度状态
        orderInfo.setProcessStatus(processStatus.name());
        // 获取订单明细列表
        List<OrderDetail> orderDetailList = orderInfo.getOrderDetailList();
        StringBuffer tradeBody = new StringBuffer();
        orderDetailList.forEach(orderDetail -> {
            tradeBody.append(orderDetail.getSkuName());
        });
        if (tradeBody.length() > 50) {
            orderInfo.setTradeBody(tradeBody.substring(0, 50));
        } else {
            orderInfo.setTradeBody(String.valueOf(tradeBody));
        }
        orderInfoMapper.insert(orderInfo);
        // 需要存完orderInfo之后才能获取id
        orderDetailList.forEach(orderDetail -> {
            // 处理订单详情业务
            orderDetail.setOrderId(orderInfo.getId());
            orderDetailMapper.insert(orderDetail);
        });
        System.out.println("id = " + orderInfo.getId());

        //发送延迟队列，如果定时未支付，取消订单
        rabbitTemplate.convertAndSend(
                DeadLetterMqConfig.exchange_dead,
                DeadLetterMqConfig.routing_dead_1,
                orderInfo.getId());

        return orderInfo.getId();
    }


    private static String getTradeKey(String userId) {
        return RedisConst.USER_KEY_PREFIX + userId + RedisConst.USER_TRADE_KEY_SUFFIX;
    }
}
