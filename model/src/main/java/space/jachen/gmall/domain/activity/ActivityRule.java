package space.jachen.gmall.domain.activity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import space.jachen.gmall.domain.base.BaseEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * 活动规则
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("activity_rule")
public class ActivityRule extends BaseEntity {
   
   private static final long serialVersionUID = 1L;

   /**
    * 类型
    */
   @TableField("activity_id")
   private Long activityId;

   /**
    * 满减金额
    */
   @TableField("condition_amount")
   private BigDecimal conditionAmount;

   /**
    * 满减件数
    */
   @TableField("condition_num")
   private Long conditionNum;

   /**
    * 优惠金额
    */
   @TableField("benefit_amount")
   private BigDecimal benefitAmount;

   /**
    * 优惠折扣
    */
   @TableField("benefit_discount")
   private BigDecimal benefitDiscount;

   /**
    * 优惠级别
    */
   @TableField("benefit_level")
   private Long benefitLevel;

   /**
    * 活动类型（1：满减，2：折扣）
    */
   @TableField(exist = false)
   private String activityType;

   /**
    * 活动skuId
    */
   @TableField(exist = false)
   private Long skuId;

   /**
    * 优惠后减少金额
    */
   @TableField(exist = false)
   private BigDecimal reduceAmount;

   /**
    * 活动对应的skuId列表
    */
   @TableField(exist = false)
   private List<Long> skuIdList;
}