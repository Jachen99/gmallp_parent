package space.jachen.gmall.domain.activity;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户秒杀记录
 */
@Data
public class UserRecode implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * skuId
	 */
	private Long skuId;

	/**
	 * userId
	 */
	private String userId;
}
