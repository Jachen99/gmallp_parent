package space.jachen.gmall.mq.receiver;

import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author JaChen
 * @date 2023/3/21 18:41
 */
@Component
@Slf4j
@SuppressWarnings("all")
public class ConfirmReceiver {
    @SneakyThrows
    @RabbitListener(bindings=@QueueBinding(
            value = @Queue(value = "queue.confirm",autoDelete = "false",durable = "true"),
            exchange = @Exchange(value = "exchange.confirm"),
            key = {"routing.confirm"}))
    public void process(Message message, Channel channel){
        log.info("RabbitListener{}", new String(message.getBody()));
        // false 确认一个消息，true 批量确认
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
