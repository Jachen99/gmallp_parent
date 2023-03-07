package space.jachen.gmall.domain.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import space.jachen.gmall.domain.base.BaseEntity;

/**
 * Sku销售属性值
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sku_sale_attr_value")
public class SkuSaleAttrValue extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 库存单元id
	 */
	@TableField("sku_id")
	private Long skuId;

	/**
	 * spu_id(冗余)
	 */
	@TableField("spu_id")
	private Long spuId;

	/**
	 * 销售属性值id
	 */
	@TableField("sale_attr_value_id")
	private Long saleAttrValueId;

}

