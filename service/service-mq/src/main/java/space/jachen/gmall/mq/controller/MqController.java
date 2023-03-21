package space.jachen.gmall.mq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.common.service.RabbitService;

/**
 * <a href="http://localhost:8282/mq/sendConfirm">测试</a>
 *
 * @author JaChen
 * @date 2023/3/21 18:31
 */
@RestController
@RequestMapping("/mq")
public class MqController {
    @Autowired
    private RabbitService rabbitService;

    /**
     * 消息发送
     */
    @GetMapping("/sendConfirm")
    public Result<String> sendConfirm() {
        rabbitService.sendMessage("exchange.confirm", "routing.confirm", "测试一号发送了消息...");

        return Result.ok();
    }
}
