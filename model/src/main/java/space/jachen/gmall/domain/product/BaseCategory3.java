package space.jachen.gmall.domain.product;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import space.jachen.gmall.domain.base.BaseEntity;

/**
 * 商品三级分类
 *
 * @author jachen
 * @since 2023-03-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseCategory3 extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 三级分类名称
     */
    @TableField("name")
    private String name;

}
