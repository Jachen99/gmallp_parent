package space.jachen.gmall.domain.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import space.jachen.gmall.domain.base.BaseEntity;

import java.util.List;


/**
 * 平台属性
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("base_attr_info")
public class BaseAttrInfo extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 属性名称
	 */
	@TableField("attr_name")
	private String attrName;

	/**
	 * 分类id
	 */
	@TableField("category_id")
	private Long categoryId;

	/**
	 * 分类层级
	 */
	@TableField("category_level")
	private Integer categoryLevel;

	/**
	 * 平台属性值集合
	 */
	@TableField(exist = false)
	private List<BaseAttrValue> attrValueList;

}

