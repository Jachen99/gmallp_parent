package space.jachen.gmall.common.service;

import com.alibaba.fastjson.JSON;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import space.jachen.gmall.common.bean.GmallCorrelationData;
import space.jachen.gmall.common.util.UuidUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author JaChen
 * @date 2023/3/21 18:25
 */
@Service
@SuppressWarnings("all")
public class RabbitService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 发送消息的方法
     * v1.1 增加消息实体传递
     *
     * @param exchange   交换机
     * @param routingKey 路由键
     * @param message    消息
     */
    public void sendMessage(String exchange, String routingKey, Object message) {
        GmallCorrelationData gmallCorrelationData = new GmallCorrelationData();
        gmallCorrelationData.setId(UuidUtils.getUUID());
        gmallCorrelationData.setMessage(message);
        gmallCorrelationData.setExchange(exchange);
        gmallCorrelationData.setRoutingKey(routingKey);
        stringRedisTemplate.opsForValue().set(
                gmallCorrelationData.getId(),
                JSON.toJSONString(gmallCorrelationData),
                10, TimeUnit.MINUTES);

        rabbitTemplate.convertAndSend(exchange, routingKey, message,gmallCorrelationData);
    }
}
