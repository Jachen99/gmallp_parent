package space.jachen.gmall.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.common.util.AuthContextHolder;
import space.jachen.gmall.domain.user.UserAddress;
import space.jachen.gmall.user.service.UserAddressService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author JaChen
 * @date 2023/3/19 22:41
 */
@RestController
@RequestMapping("/api/user")
public class UserApiController {

    @Autowired
    private UserAddressService userAddressService;


    /**
     * H5端获取用户地址接口
     * @param request
     * @return
     */
    @GetMapping("userAddress/auth/findUserAddressList")
    public Result<List<UserAddress>> findUserAddressList(HttpServletRequest request){
        String userId = AuthContextHolder.getUserId(request);
        return Result.ok(userAddressService.findUserAddressListByUserId(userId));
    }




    /**
     * 获取用户地址
     * @param userId  userId
     * @return  List<UserAddress>
     */
    @GetMapping("inner/findUserAddressListByUserId/{userId}")
    public List<UserAddress> findUserAddressListByUserId(@PathVariable("userId") String userId){
        return userAddressService.findUserAddressListByUserId(userId);
    }

}
