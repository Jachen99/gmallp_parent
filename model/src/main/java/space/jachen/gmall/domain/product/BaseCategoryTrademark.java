//
//
package space.jachen.gmall.domain.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import space.jachen.gmall.domain.base.BaseEntity;

/**
 * 商品品牌 与 商品三级分类
 * 的 中间表实体对象 aseCategoryTrademark
 * 记录了 BaseTrademark的基本数据信息
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("base_category_trademark")
public class BaseCategoryTrademark extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 三级分类编号
	 */
	@TableField("category3_id")
	private Long category3Id;

	/**
	 * 品牌id
	 */
	@TableField("trademark_id")
	private Long trademarkId;

	@TableField(exist = false)
	private BaseTrademark baseTrademark;
}

