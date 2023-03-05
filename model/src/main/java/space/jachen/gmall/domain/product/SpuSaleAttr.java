package space.jachen.gmall.domain.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import space.jachen.gmall.domain.base.BaseEntity;

import java.util.List;


/**
 * SpuSaleAttr  Spu 销售属性
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("spu_sale_attr")
public class SpuSaleAttr extends BaseEntity {
	
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
	 * 销售属性名称(冗余)
	 */
	@TableField("sale_attr_name")
	private String saleAttrName;

	/**
	 * 销售属性值对象集合
	 */
	@TableField(exist = false)
	List<SpuSaleAttrValue> spuSaleAttrValueList;

}

