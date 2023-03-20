package space.jachen.gmall.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public String getTradeNo(String userId) {
        String tradeKey = getTradeKey(userId);
        // 设置流水号
        String tradeNo = String.valueOf(UUID.randomUUID());
        redisTemplate.opsForValue().set(tradeKey,tradeNo);

        return tradeNo;
    }

    @Override
    public boolean checkTradeCode(String userId, String tradeCodeNo) {
        String tradeKey = getTradeKey(userId);
        String redisTradeNo = redisTemplate.opsForValue().get(tradeKey);
        if (!StringUtils.isEmpty(redisTradeNo)){
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
        orderInfo.setOutTradeNo("gmall"+UUID.randomUUID());
        // 失效时间
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE,1);
        orderInfo.setExpireTime(instance.getTime());
        // 进度状态
        orderInfo.setProcessStatus(ProcessStatus.UNPAID.name());
        // 获取订单明细列表
        List<OrderDetail> orderDetailList = orderInfo.getOrderDetailList();
        StringBuffer tradeBody = new StringBuffer();
        orderDetailList.forEach(orderDetail -> {
            tradeBody.append(orderDetail.getSkuName());
            // 处理订单详情业务
            orderDetail.setOrderId(orderInfo.getId());
            orderDetailMapper.insert(orderDetail);
        });
        if (tradeBody.length()>50){
            orderInfo.setTradeBody(tradeBody.substring(0,50));
        }else {
            orderInfo.setTradeBody(String.valueOf(tradeBody));
        }
        orderInfoMapper.insert(orderInfo);

        return orderInfo.getId();
    }


    private static String getTradeKey(String userId) {
        return RedisConst.USER_KEY_PREFIX+userId+RedisConst.USER_TRADE_KEY_SUFFIX;
    }
}
