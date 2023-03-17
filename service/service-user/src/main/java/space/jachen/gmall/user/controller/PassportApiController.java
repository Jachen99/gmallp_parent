package space.jachen.gmall.user.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import space.jachen.gmall.common.constant.RedisConst;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.common.util.UuidUtils;
import space.jachen.gmall.domain.user.UserInfo;
import space.jachen.gmall.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author JaChen
 * @date 2023/3/17 16:42
 */
@RestController
@RequestMapping("/api/user")
public class PassportApiController {

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate redisTemplate;


    /**
     * 退出
     * @param request   HttpServletRequest
     * @return   Result<String>
     */
    @GetMapping("/passport/logout")
    public Result<String> logout(HttpServletRequest request){
        redisTemplate.delete(RedisConst.USER_LOGIN_KEY_PREFIX + request.getHeader("token"));
        return Result.ok();
    }


    /**
     * 登录
     * @param userInfo  UserInfo
     * @param request  HttpServletRequest
     * @return  Result<Object>
     */
    @PostMapping("/passport/login")
    public Result<Object> login(@RequestBody UserInfo userInfo, HttpServletRequest request) {

        UserInfo loginInfo = userService.login(userInfo);
        if (null != loginInfo) {
            // 封装前台需要的数据
            String token = UuidUtils.getUUID();
            Map<String, String> map = new HashMap<>();
            map.put("token", token);
            map.put("nickName", loginInfo.getNickName());
            // 把userId和ip存入redis，为下次校验使用，从而实现分布式下的单点登录
            JSONObject object = new JSONObject();
            object.put("userId", userInfo.getId());
            object.put("ip", com.atguigu.gmall.common.util.IpUtil.getIpAddress(request));
            redisTemplate.opsForValue().set(
                    RedisConst.USER_LOGIN_KEY_PREFIX + "token",
                    object.toString(),
                    RedisConst.USER_KEY_TIMEOUT,
                    TimeUnit.SECONDS);
            return Result.ok(map);
        }else {
            return Result.fail().message("用户名或密码错误");
        }
    }



}
