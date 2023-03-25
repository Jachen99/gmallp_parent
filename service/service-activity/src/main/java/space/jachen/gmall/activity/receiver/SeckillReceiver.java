package space.jachen.gmall.activity.receiver;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import space.jachen.gmall.activity.mapper.SeckillGoodsMapper;
import space.jachen.gmall.activity.service.SeckillGoodsService;
import space.jachen.gmall.common.constant.MqConst;
import space.jachen.gmall.common.constant.RedisConst;
import space.jachen.gmall.common.util.DateUtil;
import space.jachen.gmall.domain.activity.SeckillGoods;
import space.jachen.gmall.domain.activity.UserRecode;

import java.util.Date;
import java.util.List;

/**
 * @author JaChen
 * @date 2023/3/24 19:46
 */
@Component
@SuppressWarnings("all")
public class SeckillReceiver {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private SeckillGoodsService seckillGoodsService;


    /**
     * 获取秒杀成功用户信息的监听器
     * @param userRecode
     * @param message
     * @param channel
     */
    @SneakyThrows
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = MqConst.QUEUE_SECKILL_USER,durable = "true",autoDelete = "false"),
                    exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_SECKILL_USER),
                    key = {MqConst.ROUTING_SECKILL_USER}
            )
    )
    public void seckillUser(UserRecode userRecode,Message message,Channel channel){

        try {
            //  判断接收过来的数据
            if (userRecode!=null){
                //  预下单处理！
                seckillGoodsService.seckillOrder(userRecode.getSkuId(),userRecode.getUserId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }





    /**
     * 监听预热秒杀商品的定时任务
     * @param message
     * @param channel
     */
    @SneakyThrows
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_TASK_1,durable = "true",autoDelete = "false"),
            exchange = @Exchange(value = MqConst.EXCHANGE_DIRECT_TASK),
            key = {MqConst.ROUTING_TASK_1}
    ))
    public void importToRedis(Message message, Channel channel){
        try {
            //  将当天的秒杀商品放入缓存！通过mapper 执行sql 语句！
            //  条件当天 ，剩余库存>0 , 审核状态 = 1
            QueryWrapper<SeckillGoods> seckillGoodsQueryWrapper = new QueryWrapper<>();
            seckillGoodsQueryWrapper.eq("status","1").gt("stock_count",0);
            // select  DATE_FORMAT(start_time,'%Y-%m-%d') from seckill_goods; yyyy-mm-dd
            seckillGoodsQueryWrapper.eq("DATE_FORMAT(start_time,'%Y-%m-%d')", DateUtil.formatDate(new Date()));
            //  获取到当天秒杀的商品列表！
            List<SeckillGoods> seckillGoodsList = seckillGoodsMapper.selectList(seckillGoodsQueryWrapper);

            //  将seckillGoodsList 这个集合数据放入缓存！
            for (SeckillGoods seckillGoods : seckillGoodsList) {
                //  考虑使用哪种数据类型，以及缓存的key！使用hash！ hset key field value hget key field
                //  定义key = SECKILL_GOODS field = skuId value = seckillGoods
                //  判断当前缓存key 中是否有 秒杀商品的skuId
                Boolean flag = redisTemplate.boundHashOps(RedisConst.SECKILL_GOODS).hasKey(seckillGoods.getSkuId().toString());
                //  判断
                if (Boolean.TRUE.equals(flag)){
                    //  表示缓存中已经当前的商品了。
                    continue;
                }
                //  没有就放入缓存     skuId 1
                redisTemplate.boundHashOps(RedisConst.SECKILL_GOODS).put(seckillGoods.getSkuId().toString(),seckillGoods);
                //  将每个商品对应的库存剩余数，放入redis-list 集合中  【保证消费消息的原子性】
                for (int i = 0; i < seckillGoods.getStockCount(); i++) {
                    //  放入list  key = seckill:stock:skuId;
                    String key = RedisConst.SECKILL_STOCK_PREFIX+seckillGoods.getSkuId();
                    redisTemplate.opsForList().leftPush(key,seckillGoods.getSkuId().toString());
                }

                //  秒杀商品在初始化的时候：状态位初始化 1
                //  publish seckillpush 46:1  | 后续业务如果说商品被秒杀完了  publish seckillpush 46:0  发布订阅
                redisTemplate.convertAndSend("seckillpush",seckillGoods.getSkuId()+":1");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //  手动确认消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }

}
