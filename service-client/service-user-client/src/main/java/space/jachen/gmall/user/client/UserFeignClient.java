package space.jachen.gmall.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import space.jachen.gmall.domain.user.UserAddress;

import java.util.List;

/**
 * @author JaChen
 * @date 2023/3/19 22:44
 */
@FeignClient(value = "service-user",fallback = UserDegradeFeignClient.class)
public interface UserFeignClient {


    public static final String BaseURL = "/api/user";

    /**
     * 获取用户地址
     * @param userId  userId
     * @return  List<UserAddress>
     */
    @GetMapping(BaseURL+"/inner/findUserAddressListByUserId/{userId}")
    public List<UserAddress> findUserAddressListByUserId(@PathVariable("userId") String userId);
}
