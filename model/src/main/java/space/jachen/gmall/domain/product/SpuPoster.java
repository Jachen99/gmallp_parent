package space.jachen.gmall.domain.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import space.jachen.gmall.domain.base.BaseEntity;

/**
 * SpuPoster 商品海报
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("spu_poster")
public class SpuPoster extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 商品id
	 */
	@TableField("spu_id")
	private Long spuId;

	/**
	 * 文件名称
	 */
	@TableField("img_name")
	private String imgName;

	/**
	 * 文件路径
	 */
	@TableField("img_url")
	private String imgUrl;

}

