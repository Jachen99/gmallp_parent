package space.jachen.gmall.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 二级分类表
 * </p>
 *
 * @author jachen
 * @since 2023-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseCategory2 implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 二级分类名称
     */
    private String name;

    /**
     * 一级分类编号
     */
    private Long category1Id;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Integer isDeleted;


}
