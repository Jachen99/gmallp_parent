package space.jachen.gmall.domain.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import space.jachen.gmall.domain.base.BaseEntity;

import java.util.List;


/**
 * 商品表 SpuInfo
 * -----------------------------------
 * spu：Standard Product Unit  销售属性
 * 标准化产品单元，商品信息聚合的最小单位；
 * ---------------------------------
 * 例如：spu = 小米13
 * ---------------------------------
 * sku：标识具体的商品，产品的唯一编号，库存量单位；
 * sku = spu + 销售属性；
 * sku = 小米13 + 白色 + 8G+128G  4299.00
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("spu_info")
public class SpuInfo extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 商品名称
	 */
	@TableField("spu_name")
	private String spuName;

	/**
	 * 商品描述(后台简述）
	 */
	@TableField("description")
	private String description;

	/**
	 * 三级分类id
	 */
	@TableField("category3_id")
	private Long category3Id;

	/**
	 * 品牌id
	 */
	@TableField("tm_id")
	private Long tmId;

	/**
	 * 销售属性集合
	 */
	@TableField(exist = false)
	private List<SpuSaleAttr> spuSaleAttrList;

	/**
	 * 商品的图片集合
	 */
	@TableField(exist = false)
	private List<SpuImage> spuImageList;

	/**
	 * 商品的海报图片集合
	 */
	@TableField(exist = false)
	private List<SpuPoster> spuPosterList;
}

