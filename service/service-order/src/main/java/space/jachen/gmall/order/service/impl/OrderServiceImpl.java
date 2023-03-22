package space.jachen.gmall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import space.jachen.gmall.common.config.DeadLetterMqConfig;
import space.jachen.gmall.common.constant.RedisConst;
import space.jachen.gmall.domain.enums.OrderStatus;
import space.jachen.gmall.domain.enums.ProcessStatus;
import space.jachen.gmall.domain.order.OrderDetail;
import space.jachen.gmall.domain.order.OrderInfo;
import space.jachen.gmall.order.mapper.OrderDetailMapper;
import space.jachen.gmall.order.mapper.OrderInfoMapper;
import space.jachen.gmall.order.service.OrderService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    @Value("${ware.url}")
    private String WARE_URL;

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
    public Long saveOrderInfo(OrderInfo orderInfo) {
        orderInfo.sumTotalAmount();
        orderInfo.setOrderStatus(OrderStatus.UNPAID.name());
        orderInfo.setCreateTime(new Date());
        orderInfo.setOutTradeNo("gmall" + UUID.randomUUID());
        // 失效时间
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 1);
        orderInfo.setExpireTime(instance.getTime());
        // 进度状态
        orderInfo.setProcessStatus(ProcessStatus.UNPAID.name());
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
