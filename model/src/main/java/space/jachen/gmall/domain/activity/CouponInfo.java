package space.jachen.gmall.domain.activity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import space.jachen.gmall.domain.base.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 优惠券信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("coupon_info")
public class CouponInfo extends BaseEntity {
   
   private static final long serialVersionUID = 1L;

   /**
    * 购物券名称
    */
   @TableField("coupon_name")
   private String couponName;

   /**
    * 购物券类型
    */
   @TableField("coupon_type")
   private String couponType;

   /**
    * 满额数
    */
   @TableField("condition_amount")
   private BigDecimal conditionAmount;

   /**
    * 满件数
    */
   @TableField("condition_num")
   private Long conditionNum;

   /**
    * 活动编号
    */
   @TableField("activity_id")
   private Long activityId;

   /**
    * 减金额
    */
   @TableField("benefit_amount")
   private BigDecimal benefitAmount;

   /**
    * 折扣
    */
   @TableField("benefit_discount")
   private BigDecimal benefitDiscount;

   /**
    * 范围类型
    */
   @TableField("range_type")
   private String rangeType;

   /**
    * 最多领用次数
    */
   @TableField("limit_num")
   private Integer limitNum;

   /**
    * 已领用次数
    */
   @TableField("taken_count")
   private Integer takenCount;

   /**
    * 可以领取的开始日期
    */
   @TableField("start_time")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private Date startTime;

   /**
    * 可以领取的结束日期
    */
   @TableField("end_time")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private Date endTime;

   /**
    * 创建时间
    */
   @TableField("create_time")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private Date createTime;

   /**
    * 修改时间
    */
   @TableField("operate_time")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private Date operateTime;

   /**
    * 过期时间
    */
   @TableField("expire_time")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
   private Date expireTime;

   /**
    * 优惠券范围描述
    */
   @TableField("range_desc")
   private String rangeDesc;

   @TableField(exist = false)
   private String couponTypeString;

   @TableField(exist = false)
   private String rangeTypeString;

   /**
    * 是否领取
    */
   @TableField(exist = false)
   private Integer isGet;

   /**
    * 购物券状态（1：未使用 2：已使用）
    */
   @TableField(exist = false)
   private String couponStatus;

   /**
    * 范围类型id
    */
   @TableField(exist = false)
   private Long rangeId;

   /**
    * 优惠券对应的skuId列表
    */
   @TableField(exist = false)
   private List<Long> skuIdList;

   /**
    * 优惠后减少金额
    */
   @TableField(exist = false)
   private BigDecimal reduceAmount;

   /**
    * 是否最优选项
    */
   @TableField(exist = false)
   private Integer isChecked = 0;

   /**
    * 是否可选
    */
   @TableField(exist = false)
   private Integer isSelect = 0;

}