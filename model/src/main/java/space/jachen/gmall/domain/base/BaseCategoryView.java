//
//
package space.jachen.gmall.domain.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品基础分类 view 对象
 *
 */
@Data
@TableName("base_category_view")
public class BaseCategoryView implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 基础分类表 id
	 */
	private Long id;

	/**
	 * 一级分类编号
	 */
	@TableField("category1_id")
	private Long category1Id;

	/**
	 * 一级分类名称
	 */
	@TableField("category1_name")
	private String category1Name;

	/**
	 * 二级分类编号
	 */
	@TableField("category2_id")
	private Long category2Id;

	/**
	 * 二级分类名称
	 */
	@TableField("category2_name")
	private String category2Name;

	/**
	 * 三级分类编号
	 */
	@TableField("category3_id")
	private Long category3Id;

	/**
	 * 三级分类名称
	 */
	@TableField("category3_name")
	private String category3Name;

}

