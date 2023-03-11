package space.jachen.gmall.common.cache;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import space.jachen.gmall.common.constant.RedisConst;
import space.jachen.gmall.common.execption.GmallException;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * 自定义注解 GmallCache切面类
 * -----------------------------
 * Redisson相关：
 * - Redisson 是一个基于 Redis 的分布式对象和服务框架，提供了一系列易于使用的工具，帮助开发者在分布式环境中使用 Redis。
 * - RedissonClient 是 Redisson 库提供的一个顶级接口，用于管理 Redisson 的所有 Redisson 对象和资源。
 * --------------------------------------------------------------------------------------------
 *  @ Around环绕通知：它集成了@Before、@AfterReturing、@AfterThrowing、@After四大通知。
 *  需要注意的是，他和其他四大通知注解最大的不同是需要手动进行接口内方法的反射后才能执行接口中的方法，
 *  换言之，@Around其实就是一个动态代理。
 * -----------------------------------
 * @author JaChen
 * @date 2023/3/11 16:10
 */
@Component
@Aspect
@SuppressWarnings("all")
public class GmallCacheAspect {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    @Around("@annotation(space.jachen.gmall.common.cache.GmallCache)")
    public Object GmallCacheMethod(ProceedingJoinPoint point) throws Throwable {

        Object obj;
        try {
            // 获取方法签名
            MethodSignature signature = (MethodSignature) point.getSignature();
            // 获取注解对象
            GmallCache gmallCache = signature.getMethod().getAnnotation(GmallCache.class);
            // 封装 dataKey
            String dataKey = gmallCache.front() + Arrays.asList(point.getArgs()) + gmallCache.next();
            // 获取 缓存中的 key
            String keyStr = (String) redisTemplate.opsForValue().get(dataKey);
            // 缓存中有key，查询缓存并返回
            if ( !StringUtils.isEmpty(keyStr) ){
                return JSON.parseObject(keyStr, signature.getReturnType());
            }else {
                // 缓存中没有key，查询数据库并返回
                RLock lock = redissonClient.getLock(gmallCache.front() + RedisConst.SKULOCK_SUFFIX);
                boolean flag = lock.tryLock(RedisConst.SKULOCK_EXPIRE_PX1, RedisConst.SKULOCK_EXPIRE_PX2, TimeUnit.SECONDS);
                if (flag){
                    try {
                        // 调proceed执行切点方法查询数据库
                        obj = point.proceed();
                        if (obj == null){
                            obj = new Object();
                        }
                        //  将缓存的数据变为 Json 的 字符串
                        redisTemplate.opsForValue().set(dataKey,JSON.toJSONString(obj),
                                RedisConst.SKUKEY_TEMPORARY_TIMEOUT,TimeUnit.SECONDS);
                        return obj;
                    } finally {
                        lock.unlock();
                    }
                }else {
                    Thread.sleep(200);
                    this.GmallCacheMethod(point);
                }
            }
        } catch (GmallException e) {
            e.printStackTrace();
            // 兜底方法
            return point.proceed(point.getArgs());
        }
        return new Object();
    }

}
