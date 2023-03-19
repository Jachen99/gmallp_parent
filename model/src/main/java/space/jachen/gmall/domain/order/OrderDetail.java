package space.jachen.gmall.domain.order;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import space.jachen.gmall.domain.base.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单明细
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("order_detail")
public class OrderDetail extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
    @TableField("order_id")
    private Long orderId;

    /**
     * sku_id
     */
    @TableField("sku_id")
    private Long skuId;

    /**
     * sku名称（冗余)
     */
    @TableField("sku_name")
    private String skuName;

    /**
     * 图片名称（冗余)
     */
    @TableField("img_url")
    private String imgUrl;

    /**
     * 购买价格(下单时sku价格）
     */
    @TableField("order_price")
    private BigDecimal orderPrice;

    /**
     * 购买个数
     */
    @TableField("sku_num")
    private Integer skuNum;

    /**
     * 是否有足够的库存
     */
    @TableField(exist = false)
    private String hasStock;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    /**
     * 实际支付金额
     */
    @TableField("split_total_amount")
    private BigDecimal splitTotalAmount;

    /**
     * 促销分摊金额
     */
    @TableField("split_activity_amount")
    private BigDecimal splitActivityAmount;

    /**
     * 优惠券分摊金额
     */
    @TableField("split_coupon_amount")
    private BigDecimal splitCouponAmount;

}
