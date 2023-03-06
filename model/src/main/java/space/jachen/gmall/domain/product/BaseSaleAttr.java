package space.jachen.gmall.domain.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import space.jachen.gmall.domain.base.BaseEntity;

/**
 * 基本销售属性
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("base_sale_attr")
public class BaseSaleAttr extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 销售属性名称
	 */
	@TableField("name")
	private String name;

}

