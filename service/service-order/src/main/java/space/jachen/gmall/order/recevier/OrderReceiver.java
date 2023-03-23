package space.jachen.gmall.order.recevier;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import space.jachen.gmall.common.config.DeadLetterMqConfig;
import space.jachen.gmall.common.constant.MqConst;
import space.jachen.gmall.domain.enums.ProcessStatus;
import space.jachen.gmall.domain.order.OrderInfo;
import space.jachen.gmall.order.service.OrderService;

import java.io.IOException;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/3/21 22:40
 */
@Component
public class OrderReceiver {
    @Autowired
    private OrderService orderService;



    /**
     * 扣减库存成功，更新订单状态
     * @param msgJson
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_WARE_ORDER, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_WARE_ORDER),
            key = {MqConst.ROUTING_WARE_ORDER}
    ))
    public void updateOrderStatus(String msgJson, Message message, Channel channel) throws IOException {
        if (!StringUtils.isEmpty(msgJson)) {
            Map<String, Object> map = JSON.parseObject(msgJson, Map.class);
            String orderId = (String) map.get("orderId");
            String status = (String) map.get("status");
            // 如果订单状态为 DEDUCTED 已减库存 则成功
            if ("DEDUCTED".equals(status)) {
                // 减库存成功 修改订单状态为 待发货
                orderService.updateOrderStatus(Long.parseLong(orderId), ProcessStatus.WAITING_DELEVER);
            } else {
                /*
                    减库存失败 修改订单状态为 库存异常 【解决方案：远程调用其他仓库查看是否有库存】
                    true:   orderService.sendOrderStatus(orderId); orderService.updateOrderStatus(orderId, ProcessStatus.NOTIFIED_WARE);
                    false:  1.  补货  | 2.   人工客服。
                 */
                orderService.updateOrderStatus(Long.parseLong(orderId), ProcessStatus.STOCK_EXCEPTION);
            }
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }


    /**
     * 更新订单的状态
     * @param orderId
     * @param message
     * @param channel
     */
    @SneakyThrows
    @RabbitListener(bindings=@QueueBinding(
            value= @Queue(value= MqConst.QUEUE_PAYMENT_PAY,durable= "true",autoDelete = "false"),
            exchange= @Exchange(value= MqConst.EXCHANGE_DIRECT_PAYMENT_PAY),
            key= {MqConst.ROUTING_PAYMENT_PAY})
    )
    public void upOrder(Long orderId ,Message message,Channel channel) {
        //断orderId不为空
        if (null != orderId) {
            //更新订单的状态，还有进度的状态
            OrderInfo orderInfo = orderService.getById(orderId);
            //判断状态了准备更新据
            if (null != orderInfo && orderInfo.getOrderStatus().equals(ProcessStatus.UNPAID.getOrderStatus().name()))
                orderService.updateOrderStatus(orderId, ProcessStatus.PAID);
        }
        //手动确以
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }




    //  监听的消息
    @SneakyThrows
    @RabbitListener(queues = DeadLetterMqConfig.queue_dead_2)
    public void cancelOrder(Long orderId , Message message, Channel channel){
        //  判断当前订单Id 不能为空
        try {
            if (orderId!=null){
                //  发过来的是订单Id，那么你就需要判断一下当前的订单是否已经支付了。
                //  未支付的情况下：关闭订单
                //  根据订单Id 查询orderInfo select * from order_info where id = orderId
                //  利用这个接口IService  实现类ServiceImpl 完成根据订单Id 查询订单信息 ServiceImpl 类底层还是使用的mapper
                OrderInfo orderInfo = orderService.getById(orderId);
                //  判断支付状态,进度状态
                if (orderInfo!=null && "UNPAID".equals(orderInfo.getOrderStatus())
                        && "UNPAID".equals(orderInfo.getProcessStatus())){
                    //  关闭订单
                    //  int i = 1/0;
                    orderService.execExpiredOrder(orderId);
                }
            }
        } catch (Exception e) {
            //  消息没有正常被消费者处理： 记录日志后续跟踪处理!

            e.printStackTrace();
        }
        //  手动确认消息 如果不确认，有可能会到消息残留。
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

}
