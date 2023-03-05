package space.jachen.gmall.domain.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import space.jachen.gmall.domain.base.BaseEntity;

/**
 * Spu 图片
 * 商品图片
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("spu_image")
public class SpuImage extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 商品id
	 */
	@TableField("spu_id")
	private Long spuId;

	/**
	 * 图片名称
	 */
	@TableField("img_name")
	private String imgName;

	/**
	 * 图片路径
	 */
	@TableField("img_url")
	private String imgUrl;

}

