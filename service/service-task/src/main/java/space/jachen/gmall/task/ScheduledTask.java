package space.jachen.gmall.task;

/**
 * @author JaChen
 * @date 2023/3/24 19:36
 */

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import space.jachen.gmall.common.constant.MqConst;
import space.jachen.gmall.common.service.RabbitService;


@Component
@EnableScheduling
@Slf4j
public class ScheduledTask {
    @Autowired
    private RabbitService rabbitService;
    /**
     * 每天凌晨1点执行
     */
    //@Scheduled(cron = "0/30 * * * * ?")
    @Scheduled(cron = "0 0 1 * * ?")
    public void task1() {
        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_TASK,  MqConst.ROUTING_TASK_1, "");
    }
}
