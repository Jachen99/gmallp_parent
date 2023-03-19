package space.jachen.gmall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import space.jachen.gmall.domain.user.UserAddress;
import space.jachen.gmall.user.mapper.UserAddressMapper;
import space.jachen.gmall.user.service.UserAddressService;

import java.util.List;

/**
 * @author JaChen
 * @date 2023/3/19 22:39
 */
@Service
public class UserAddressServiceImpl implements UserAddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;
    @Override
    public List<UserAddress> findUserAddressListByUserId(String userId) {
        LambdaQueryWrapper<UserAddress> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAddress::getUserId, userId);
        return userAddressMapper.selectList(queryWrapper);
    }
}
