//
//
package space.jachen.gmall.domain.product;

import lombok.Data;

import java.util.List;

/**
 * 三级分类编号和品牌id集合 View
 *
 */
@Data
public class CategoryTrademarkVo {

	/**
	 * 三级分类编号
	 */
	private Long category3Id;

	/**
	 * 品牌id
	 */
	private List<Long> trademarkIdList;

}

