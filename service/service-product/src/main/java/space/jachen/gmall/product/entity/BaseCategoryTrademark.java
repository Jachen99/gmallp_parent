package space.jachen.gmall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 三级分类表
 * </p>
 *
 * @author jachen
 * @since 2023-03-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseCategoryTrademark implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 三级级分类id
     */
    private Long category3Id;

    /**
     * 品牌id
     */
    private Long trademarkId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer isDeleted;


}
