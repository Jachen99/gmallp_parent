package space.jachen.gmall.domain.cart;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import space.jachen.gmall.domain.activity.CouponInfo;
import space.jachen.gmall.domain.base.BaseEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("cart_info")
public class CartInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    private String userId;

    /**
     * skuId
     */
    private Long skuId;

    /**
     * cartPrice
     */
    private BigDecimal cartPrice;

    /**
     * 数量
     */
    private Integer skuNum;

    /**
     * 图片文件
     */
    private String imgUrl;

    /**
     * sku名称
     */
    private String skuName;

    /**
     * isChecked
     */
    private Integer isChecked = 1;

    /**
     * 实时价格
     */
    BigDecimal skuPrice;

    /**
     * 优惠券信息列表
     */
    @TableField(exist = false)
    private List<CouponInfo> couponInfoList;

}
