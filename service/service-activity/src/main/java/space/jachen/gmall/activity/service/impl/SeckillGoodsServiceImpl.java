package space.jachen.gmall.activity.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import space.jachen.gmall.activity.mapper.SeckillGoodsMapper;
import space.jachen.gmall.activity.service.SeckillGoodsService;
import space.jachen.gmall.activity.utils.CacheHelper;
import space.jachen.gmall.common.constant.RedisConst;
import space.jachen.gmall.common.util.MD5;
import space.jachen.gmall.domain.activity.OrderRecode;
import space.jachen.gmall.domain.activity.SeckillGoods;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author JaChen
 * @date 2023/3/24 20:04
 */

@Service
@SuppressWarnings("all")
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;


    /**
     * 查询全部
     */
    @Override
    public List<SeckillGoods> findAll() {
        List list = redisTemplate.boundHashOps(RedisConst.SECKILL_GOODS).values();
        return list;
    }

    /**
     * 根据ID获取实体
     * @param id
     * @return
     */
    @Override
    public SeckillGoods getSeckillGoods(Long id) {
        return (SeckillGoods) redisTemplate.boundHashOps(RedisConst.SECKILL_GOODS).get(id.toString());
    }


    /**
     * 秒杀商品预下单
     * @param skuId
     * @param userId
     */
    @Override
    public void seckillOrder(Long skuId, String userId) {
        // 获取缓存中秒杀商品状态位
        String status = CacheHelper.get(skuId.toString());
        if (!"1".equals(status)) return;
        // 判断用户是否下单  防止超卖
        Boolean ifAbsent = redisTemplate.opsForValue().setIfAbsent(RedisConst.SECKILL_USER + userId, skuId,
                RedisConst.SECKILL__TIMEOUT, TimeUnit.SECONDS);
        if (!ifAbsent) return;
        // 进行秒杀商品预下单 goodsId不存在则已卖光  存在可以进行下单
        String goodsId = (String) redisTemplate.boundListOps(RedisConst.SECKILL_STOCK_PREFIX + skuId).rightPop();
        if (StringUtils.isEmpty(goodsId)) {
            //商品售罄，更新状态位
            redisTemplate.convertAndSend("seckillpush", skuId+":0");return;
        }
        // 保存预订单信息
        OrderRecode orderRecode = new OrderRecode();
        orderRecode.setUserId(userId);
        orderRecode.setNum(1);
        orderRecode.setSeckillGoods(getSeckillGoods(skuId));
        // 生成预订单码
        String orderStr = MD5.encrypt(userId + skuId);
        orderRecode.setOrderStr(orderStr);
        // 将预订单存入redis
        redisTemplate.boundHashOps(RedisConst.SECKILL_ORDERS).put(orderRecode.getUserId(), orderRecode);

        //更新库存
        this.updateStockCount(orderRecode.getSeckillGoods().getSkuId());
    }

    /**
     * 更新库存
     * @param skuId
     */
    @Override
    public void updateStockCount(Long skuId) {

        Lock lock = new ReentrantLock();
        lock.lock();
        try {
            // 获取商品库存数
            Long size = redisTemplate.opsForList().size(RedisConst.SECKILL_STOCK_PREFIX + skuId);
            // 减少库存数的方式----减少压力!
            if (size%2==0){
                SeckillGoods seckillGoods = this.getSeckillGoods(skuId);
                seckillGoods.setStockCount(size.intValue());
                seckillGoodsMapper.updateById(seckillGoods);

                // 更新缓存 
                redisTemplate.boundHashOps(RedisConst.SECKILL_GOODS).put(seckillGoods.getSkuId().toString(),seckillGoods);
            }
        } catch (Exception e) {
            lock.unlock();
        }

    }





}
