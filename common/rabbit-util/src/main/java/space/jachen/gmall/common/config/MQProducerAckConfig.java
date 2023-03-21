package space.jachen.gmall.common.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import space.jachen.gmall.common.bean.GmallCorrelationData;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 发送消息确认
 * ConfirmCallback  只确认消息是否正确到达 Exchange 中
 * ReturnCallback   消息没有正确到达队列时触发回调，如果正确到达队列不执行
 *
 * @author JaChen
 * @date 2023/3/21 18:02
 */
@Component
@Slf4j
public class MQProducerAckConfig implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 修饰一个非静态的void（）方法,在服务器加载Servlet的时候运行，并且只会被服务器执行一次在构造函数之后执行，init（）方法之前执行。
     */
    @PostConstruct
    public void init(){
        this.rabbitTemplate.setConfirmCallback(this);
        this.rabbitTemplate.setReturnCallback(this);
    }

    /**
     * Confirmation callback.
     * @param correlationData correlation data for the callback.
     * @param ack true for ack, false for nack
     * @param cause An optional cause, for nack, when available, otherwise null.
     */
    @Override
    public void confirm(@Nullable CorrelationData correlationData, boolean ack, @Nullable String cause) {
        log.info("confirm()方法执行了...");
        if (ack){
            log.info("消息{}发送成功", JSON.toJSONString(correlationData));
        }else {
            log.error("消息{}因为{}发送失败", JSON.toJSONString(correlationData), cause);
            // 消息发送失败重试方法
            this.retrySendMsg(correlationData);
        }
    }

    /**
     * 重试发送的方法
     * @param correlationData  CorrelationData消息实体
     */
    @SuppressWarnings("all")
    private void retrySendMsg(CorrelationData correlationData) {
        GmallCorrelationData gmallCorrelationData = (GmallCorrelationData) correlationData;
        // 获取消息的重试次数
        int retryCount = gmallCorrelationData.getRetryCount();
        if (retryCount>3){
            // 不需要重试了
            log.error("重试次数已到，发送消息失败{}", JSON.toJSONString(gmallCorrelationData));
        }else {
            // 进行重试
            gmallCorrelationData.setRetryCount(++retryCount);
            log.info("消息已经重试了{}次", gmallCorrelationData.getRetryCount());
            this.stringRedisTemplate.opsForValue().set(
                    gmallCorrelationData.getId(),
                    JSON.toJSONString(gmallCorrelationData),
                    10, TimeUnit.MINUTES);
            //  调用发送消息方法 重新发送消息
            this.rabbitTemplate.convertAndSend(
                    gmallCorrelationData.getExchange(),
                    gmallCorrelationData.getRoutingKey(),
                    gmallCorrelationData.getMessage(),
                    gmallCorrelationData);
        }
    }

    /**
     * Returned message callback.
     * @param message the returned message.
     * @param replyCode the reply code.
     * @param replyText the reply text.
     * @param exchange the exchange.
     * @param routingKey the routing key.
     */
    @Override
    @SuppressWarnings("all")
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error("returnedMessage()方法执行了...");
        log.error("消息主体{}", new String(message.getBody()));
        log.error("应答码{}", String.valueOf(replyCode));
        log.error("应答描述{}", replyText);
        log.error("消息的交换机{}", exchange);
        log.error("消息的路由key{}", routingKey);
        //  获取这个CorrelationData对象的Id  spring_returned_message_correlation
        String correlationDataId = (String) message.getMessageProperties().getHeaders().get("spring_returned_message_correlation");
        //  因为在发送消息的时候，已经将数据存储到缓存，通过 correlationDataId 来获取缓存的数据
        String strJson = this.stringRedisTemplate.opsForValue().get(correlationDataId);
        //  消息没有到队列的时候，则会调用重试发送方法
        GmallCorrelationData gmallCorrelationData = JSON.parseObject(strJson,GmallCorrelationData.class);

        //  调用方法  gmallCorrelationData 这对象中，至少的有，交换机，路由键，消息等内容.
        this.retrySendMsg(gmallCorrelationData);
    }
}
