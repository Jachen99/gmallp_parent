package space.jachen.gmall.domain.activity;

import lombok.Data;

import java.io.Serializable;

/**
 * 秒杀商品订单记录
 */
@Data
public class OrderRecode implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	private String userId;

	/**
	 * 秒杀商品实体
	 */
	private SeckillGoods seckillGoods;

	/**
	 * 数量
	 */
	private Integer num;

	/**
	 * 订单状态
	 */
	private String orderStr;
}
