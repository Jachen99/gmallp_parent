package space.jachen.gmall.user.client;

import org.springframework.stereotype.Component;
import space.jachen.gmall.domain.user.UserAddress;

import java.util.List;

/**
 * @author JaChen
 * @date 2023/3/19 22:44
 */
@Component
public class UserDegradeFeignClient implements UserFeignClient{
    @Override
    public List<UserAddress> findUserAddressListByUserId(String userId) {
        return null;
    }
}
