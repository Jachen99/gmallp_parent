package space.jachen.gmall.domain.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import space.jachen.gmall.domain.base.BaseEntity;


/**
 * SpuSaleAttrValue  spu 销售属性值
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("spu_sale_attr_value")
public class SpuSaleAttrValue extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 商品id
	 */
	@TableField("spu_id")
	private Long spuId;

	/**
	 * 销售属性id
	 */
	@TableField("base_sale_attr_id")
	private Long baseSaleAttrId;

	/**
	 * 销售属性值名称
	 */
	@TableField("sale_attr_value_name")
	private String saleAttrValueName;

	/**
	 * 销售属性名称(冗余)
	 */
	@TableField("sale_attr_name")
	private String saleAttrName;

	// 是否是默认选中状态
	/*@TableField("sale_attr_name")
	String isChecked;*/
	@TableField(exist = false)
	String isChecked;

}

