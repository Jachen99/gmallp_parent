package space.jachen.gmall.task.handler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import space.jachen.gmall.common.constant.MqConst;

/**
 * @author JaChen
 * @date 2023/3/24 20:14
 */
@Component
@Slf4j
public class XxlJobHandler {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送定时消息的方法
     * @param param
     * @return
     */
    @XxlJob(value = "seckillHandler")
    public ReturnT<String> execute(String param){
        log.info("execute方法执行了 ====》 {}" , param);
        rabbitTemplate.convertAndSend(MqConst.EXCHANGE_DIRECT_TASK,  MqConst.ROUTING_TASK_1, "");

        return ReturnT.SUCCESS;
    }

    @XxlJob(value = "seckillOverHandler")
    public ReturnT<String> execute1(String param){
        rabbitTemplate.convertAndSend(MqConst.EXCHANGE_DIRECT_TASK, MqConst.ROUTING_TASK_18, "");

        return ReturnT.SUCCESS;
    }


}
