package space.jachen.gmall.activity.service;

import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.domain.activity.SeckillGoods;

import java.util.List;

/**
 * @author JaChen
 * @date 2023/3/24 20:03
 */
public interface SeckillGoodsService {


    /***
     * 根据商品id与用户ID查看订单信息
     * @param skuId
     * @param userId
     * @return
     */
    @SuppressWarnings("all")
    Result checkOrder(Long skuId, String userId);



    /**
     * 返回全部列表
     * @return
     */
    List<SeckillGoods> findAll();

    /**
     * 根据ID获取实体
     * @param id
     * @return
     */
    SeckillGoods getSeckillGoods(Long id);


    /**
     * 秒杀商品预下单
     * @param skuId
     * @param userId
     */
    void seckillOrder(Long skuId, String userId);

    /**
     * 更新库存
     * @param skuId
     */
    void updateStockCount(Long skuId);
}
