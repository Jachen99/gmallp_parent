package space.jachen.gmall.activity.service;

import space.jachen.gmall.domain.activity.SeckillGoods;

import java.util.List;

/**
 * @author JaChen
 * @date 2023/3/24 20:03
 */
public interface SeckillGoodsService {

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


}
