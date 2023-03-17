package space.jachen.gmall.domain.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import space.jachen.gmall.domain.base.BaseEntity;

/**
 * 用户地址
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("user_address")
public class UserAddress extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户地址
     */
    @TableField("user_address")
    private String userAddress;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 收件人
     */
    @TableField("consignee")
    private String consignee;

    /**
     * 联系方式
     */
    @TableField("phone_num")
    private String phoneNum;

    /**
     * 是否是默认
     */
    @TableField("is_default")
    private String isDefault;

}

