package space.jachen.gmall.activity.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import space.jachen.gmall.activity.service.SeckillGoodsService;
import space.jachen.gmall.common.constant.RedisConst;
import space.jachen.gmall.domain.activity.SeckillGoods;

import java.util.List;

/**
 * @author JaChen
 * @date 2023/3/24 20:04
 */

@Service
@SuppressWarnings("all")
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    @Autowired
    private RedisTemplate redisTemplate;


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
}
