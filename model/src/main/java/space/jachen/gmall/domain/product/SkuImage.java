package space.jachen.gmall.domain.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import space.jachen.gmall.domain.base.BaseEntity;

/**
 * Sku图片
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sku_image")
public class SkuImage extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 商品id
	 */
	@TableField("sku_id")
	private Long skuId;

	/**
	 * 图片名称（冗余）
	 */
	@TableField("img_name")
	private String imgName;

	/**
	 * 图片路径(冗余)
	 */
	@TableField("img_url")
	private String imgUrl;

	/**
	 * 商品图片id
	 */
	@TableField("spu_img_id")
	private Long spuImgId;

	/**
	 * 是否默认
	 */
	@TableField("is_default")
	private String isDefault;

}

