package space.jachen.gmall.mq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.gmall.common.config.DeadLetterMqConfig;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.common.service.RabbitService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <a href="http://localhost:8282/mq/sendConfirm">测试</a>
 *
 * @author JaChen
 * @date 2023/3/21 18:31
 */
@RestController
@RequestMapping("/mq")
@Slf4j
public class MqController {
    @Autowired
    private RabbitService rabbitService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息到 死信队列
     */
    @GetMapping("/sendDeadLetter")
    public Result<String> sendDeadLetter() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.rabbitTemplate.convertAndSend(DeadLetterMqConfig.exchange_dead, DeadLetterMqConfig.routing_dead_1, "ok");
        log.info("{}Delay sent.",sdf.format(new Date()));

        return Result.ok();
    }


    /**
     * 消息发送
     */
    @GetMapping("/sendConfirm")
    public Result<String> sendConfirm() {
        rabbitService.sendMessage("exchange.confirm", "routing.confirm", "测试一号发送了消息...");

        return Result.ok();
    }
}
