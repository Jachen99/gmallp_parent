package space.jachen.gmall.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import space.jachen.gmall.common.result.Result;
import space.jachen.gmall.common.result.ResultCodeEnum;
import space.jachen.gmall.gateway.constant.RedisConst;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author JaChen
 * @date 2023/3/17 18:34
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${authUrls.url}")
    private String authUrls;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //获取用户请求
        ServerHttpRequest request = exchange.getRequest();
        //获取用户响应信息
        ServerHttpResponse response = exchange.getResponse();
        // 获取uri
        String path = request.getURI().getPath();

        // 1、内部接口拦截
        if (antPathMatcher.match("/**/inner/**",path)){
            // 拒接访问
            return out(response);
        }
        // 获取userId
        String userId= this.getUserId(request);

        // 2、认证资源拦截
        if (antPathMatcher.match("/**/auth/**",path)){
            if (StringUtils.isEmpty(userId)){

                return out(response);
            }
        }

        // 3、白名单拦截 (Web资源)  authUrls:配置中心配置的
        if (!StringUtils.isEmpty(authUrls)){
            //截取遍历白名单 trade.html,myOrder.html ,list.html
            for (String url : authUrls.split(",")) {
                if(path.contains(url) && StringUtils.isEmpty(userId)){
                    // 303状态码表示由于请求对应的资源存在着另一个URI，应使用重定向获取请求的资源
                    response.setStatusCode(HttpStatus.SEE_OTHER);
                    response.getHeaders().set(
                            HttpHeaders.LOCATION,
                            "http://www.gmall.com/login.html?originUrl="+request.getURI()
                    );

                    return response.setComplete();
                }
            }
        }

        //获取临时用户id
        String userTempId = getUserTempId(request);
        // 把 userId 或 userTempId 响应后端服务
        if(!StringUtils.isEmpty(userId)||!StringUtils.isEmpty(userTempId)){
            if(!StringUtils.isEmpty(userId)){
                request.mutate().header("userId",userId).build();
            }
            if(!StringUtils.isEmpty(userTempId)){
                request.mutate().header("userTempId",userTempId).build();
            }
            return chain.filter(exchange.mutate().request(request).build()) ;
        }

        return chain.filter(exchange);
    }

    /**
     * 获取临时userId
     * @param request  ServerHttpRequest
     * @return  String
     */
    private String getUserTempId(ServerHttpRequest request) {
        // 获取 临时userId
        String userTempId = "";
        List<String> tokenList = request.getHeaders().get("userTempId");
        if (!CollectionUtils.isEmpty(tokenList)){

            userTempId = tokenList.get(0);
        }else {
            HttpCookie cookie = request.getCookies().getFirst("userTempId");
            if (null!=cookie){

                userTempId = cookie.getValue();
            }
        }
        return userTempId;
    }


    /**
     * 获取userId
     * @param request  ServerHttpRequest
     * @return  String
     */
    private String getUserId(ServerHttpRequest request) {
        // 获取 token
        String token = "";
        List<String> tokenList = request.getHeaders().get("token");
        if (!CollectionUtils.isEmpty(tokenList)){

            token = tokenList.get(0);
        }else {
            HttpCookie cookie = request.getCookies().getFirst("token");
            if (null!=cookie){

                token = cookie.getValue();
            }
        }

        // 通过 token 获取 userId
        if(!StringUtils.isEmpty(token)){
            String strJson = redisTemplate.opsForValue().get(RedisConst.USER_LOGIN_KEY_PREFIX + token);
            JSONObject jsonObject = JSONObject.parseObject(strJson);
            String ip = (String) jsonObject.get("ip");
            String userId = jsonObject.getString("userId");
            if (!com.atguigu.gmall.common.util.IpUtil.getGatwayIpAddress(request).equals(ip)){

                return ResultCodeEnum.SECKILL_ILLEGAL.getMessage();
            }

            return userId;
        }

        // token为空，未登录
        return "";
    }


    /**
     * 设置拒绝响应内容
     *
     * @param response ServerHttpResponse
     * @return Mono<Void>
     */
    private Mono<Void> out(ServerHttpResponse response) {

        //中文乱码处理
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        //设置同意异常结果
        String message = JSONObject.toJSONString(Result.build(null, ResultCodeEnum.PERMISSION));
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        DataBuffer wrap = response.bufferFactory().wrap(bytes);

        return response.writeWith(Mono.just(wrap));
    }

    @Override
    public int getOrder() {
        return 11;
    }
}
