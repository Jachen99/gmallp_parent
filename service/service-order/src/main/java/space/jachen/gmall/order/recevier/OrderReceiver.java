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
import space.jachen.gmall.domain.payment.PaymentInfo;
import space.jachen.gmall.order.service.OrderService;
import space.jachen.gmall.payment.PaymentFeignClient;

import java.io.IOException;
import java.util.Map;

/**
 * @author JaChen
 * @date 2023/3/21 22:40
 */
@Component
@SuppressWarnings("all")
public class OrderReceiver {
    @Autowired
    private OrderService orderService;
    @Autowired
    private PaymentFeignClient paymentFeignClient;



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


    /**
     * 监听关闭订单
     * @param orderId
     * @param message
     * @param channel
     */
    @SneakyThrows
    @RabbitListener(queues = DeadLetterMqConfig.queue_dead_2)
    public void cancelOrder(Long orderId , Message message, Channel channel){
        try {
            //  判断订单id 是否存在！
            if (orderId!=null){
                //  根据订单Id 查询订单对象
                OrderInfo orderInfo = orderService.getById(orderId);
                //  判断
                if(orderInfo!=null && "UNPAID".equals(orderInfo.getOrderStatus()) && "UNPAID".equals(orderInfo.getProcessStatus())){
                    //  关闭过期订单！ 还需要关闭对应的 paymentInfo ，还有alipay.
                    //  orderService.execExpiredOrder(orderId);
                    //  查询paymentInfo 是否存在！
                    PaymentInfo paymentInfo = paymentFeignClient.getPaymentInfo(orderInfo.getOutTradeNo());
                    //  判断 用户点击了扫码支付
                    if(paymentInfo!=null && "UNPAID".equals(paymentInfo.getPaymentStatus())){

                        //  查看是否有支付宝交易记录！
                        Boolean flag = paymentFeignClient.checkPayment(orderId);
                        //  判断
                        if (flag){
                            //  flag = true , 有交易记录
                            //  调用关闭接口！ 扫码未支付这样才能关闭成功！
                            Boolean result = paymentFeignClient.closePay(orderId);
                            //  判断
                            if (result){
                                //  result = true; 关闭成功！未付款！需要关闭orderInfo， paymentInfo，Alipay
                                orderService.execExpiredOrder(orderId,"2");
                            }else {
                                //  result = false; 表示付款！
                                //  说明已经付款了！ 正常付款成功都会走异步通知！
                            }
                        }else {
                            //  没有交易记录，不需要关闭支付！  需要关闭orderInfo， paymentInfo
                            orderService.execExpiredOrder(orderId,"2");
                        }

                    }else {
                        //  只关闭订单orderInfo！
                        orderService.execExpiredOrder(orderId,"1");
                    }
                }
            }

        } catch (Exception e) {
            //  写入日志...
            e.printStackTrace();
        }
        //  手动确认
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

}
