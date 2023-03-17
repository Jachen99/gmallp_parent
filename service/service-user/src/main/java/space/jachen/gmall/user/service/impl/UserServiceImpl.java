package space.jachen.gmall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import space.jachen.gmall.domain.user.UserInfo;
import space.jachen.gmall.user.mapper.UserInfoMapper;
import space.jachen.gmall.user.service.UserService;

/**
 * @author JaChen
 * @date 2023/3/17 16:14
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfo login(UserInfo userInfo) {

        // 查询数据库校验用户信息
        String passwd = DigestUtils.md5DigestAsHex(userInfo.getPasswd().getBytes());
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getLoginName, userInfo.getLoginName());
        wrapper.eq(UserInfo::getPasswd, passwd);
        return userInfoMapper.selectOne(wrapper);
    }


}
