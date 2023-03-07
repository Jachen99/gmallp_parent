package space.jachen.gmall.domain.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import space.jachen.gmall.domain.base.BaseEntity;

/**
 * Sku平台属性值 SkuAttrValue
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sku_attr_value")
public class SkuAttrValue extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 属性id（冗余)
	 */
	@TableField("attr_id")
	private Long attrId;

	/**
	 * 属性值id
	 */
	@TableField("value_id")
	private Long valueId;

	/**
	 * skuId
	 */
	@TableField("sku_id")
	private Long skuId;

}

