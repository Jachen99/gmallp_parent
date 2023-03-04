package space.jachen.gmall.domain.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import space.jachen.gmall.domain.base.BaseEntity;

/**
 * 平台属性值
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("base_attr_value")
public class BaseAttrValue extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 属性值名称
	 */
	@TableField("value_name")
	private String valueName;

	/**
	 * 属性id
	 */
	@TableField("attr_id")
	private Long attrId;
}

