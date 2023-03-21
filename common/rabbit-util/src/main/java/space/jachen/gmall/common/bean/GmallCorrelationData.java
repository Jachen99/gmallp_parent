package space.jachen.gmall.common.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.amqp.rabbit.connection.CorrelationData;

/**
 * 封装自定义消息实体
 *
 * @author JaChen
 * @date 2023/3/21 19:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GmallCorrelationData extends CorrelationData {
    /**
     * 消息
     */
    private volatile Object message;
    /**
     * 交换机
     */
    private volatile String exchange;
    /**
     * 路由键
     */
    private volatile String routingKey;
    /**
     重试次数
     */
    private volatile int retryCount = 0;
    /**
     * 消息类型  是否是延迟消息
     */
    private volatile boolean isDelay = false;
    /**
     * 延迟时间
     */
    private volatile int delayTime = 10;

}
