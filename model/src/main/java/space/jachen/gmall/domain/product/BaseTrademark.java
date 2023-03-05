package space.jachen.gmall.domain.product;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import space.jachen.gmall.domain.base.BaseEntity;

/**
 * 商品品牌
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("base_trademark")
public class BaseTrademark extends BaseEntity {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 属性值
	 */
	@TableField("tm_name")
	private String tmName;

	/**
	 * 品牌logo的图片路径
	 */
	@TableField("logo_url")
	private String logoUrl;

}

