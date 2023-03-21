package space.jachen.gmall.mq.receiver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import space.jachen.gmall.common.config.DeadLetterMqConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author JaChen
 * @date 2023/3/21 20:47
 */
@Component
@Configuration
@Slf4j
public class DeadLetterReceiver {

    @RabbitListener(queues = DeadLetterMqConfig.queue_dead_2)
    public void get(String msg) {
        log.info("Receive{}", msg);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("Receive queue_dead_2{}\tDelay rece{}", sdf.format(new Date()), msg);
    }
}
