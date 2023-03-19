package space.jachen.gmall.user.service;

import space.jachen.gmall.domain.user.UserAddress;

import java.util.List;

/**
 * @author JaChen
 * @date 2023/3/19 22:38
 */
public interface UserAddressService {

    /**
     * 根据用户Id 查询用户的收货地址列表！
     * @param userId  userId
     * @return   List<UserAddress>
     */
    List<UserAddress> findUserAddressListByUserId(String userId);

}
