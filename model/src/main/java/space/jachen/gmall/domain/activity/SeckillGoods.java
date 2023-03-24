package space.jachen.gmall.domain.activity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import space.jachen.gmall.domain.base.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 秒杀商品实体
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("seckill_goods")
public class SeckillGoods extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	/**
	 * spuId
	 */
	@TableField("spu_id")
	private Long spuId;

	/**
	 * skuId
	 */
	@TableField("sku_id")
	private Long skuId;

	/**
	 * 标题
	 */
	@TableField("sku_name")
	private String skuName;

	/**
	 * 商品图片
	 */
	@TableField("sku_default_img")
	private String skuDefaultImg;

	/**
	 * 原价格
	 */
	@TableField("price")
	private BigDecimal price;

	/**
	 * 秒杀价格
	 */
	@TableField("cost_price")
	private BigDecimal costPrice;

	/**
	 * 添加日期
	 */
	@TableField("create_time")
	private Date createTime;

	/**
	 * 审核日期
	 */
	@TableField("check_time")
	private Date checkTime;

	/**
	 * 审核状态
	 */
	@TableField("status")
	private String status;

	/**
	 * 开始时间
	 */
	@TableField("start_time")
	private Date startTime;

	/**
	 * 结束时间
	 */
	@TableField("end_time")
	private Date endTime;

	/**
	 * 秒杀商品数
	 */
	@TableField("num")
	private Integer num;

	/**
	 * 剩余库存数
	 */
	@TableField("stock_count")
	private Integer stockCount;

	/**
	 * 描述
	 */
	@TableField("sku_desc")
	private String skuDesc;

}

