package space.jachen.gmall.user.service;

import space.jachen.gmall.domain.user.UserInfo;

/**
 * @author JaChen
 * @date 2023/3/17 11:05
 */
public interface UserService {

    /**
     * 登录方法
     * @param userInfo UserInfo
     * @return UserInfo
     */
    UserInfo login(UserInfo userInfo);

}
