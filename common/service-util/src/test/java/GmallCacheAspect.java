//import annotation.GmallCache;
//import com.alibaba.fastjson.JSON;
//import jdk.management.resource.internal.ApproverGroup;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import space.jachen.gmall.common.constant.RedisConst;
//
//import java.lang.reflect.Method;
//import java.util.Arrays;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author JaChen
// * @date 2023/3/11 16:14
// * ------------------------------------
// * @ Around环绕通知：它集成了@Before、@AfterReturing、@AfterThrowing、@After四大通知。
// * 需要注意的是，他和其他四大通知注解最大的不同是需要手动进行接口内方法的反射后才能执行接口中的方法，
// * 换言之，@Around其实就是一个动态代理。
// */
//@Aspect
//@Component
//public class GmallCacheAspect {
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//    @Autowired
//    private RedissonClient redissonClient;
//
//
//    @Around(annotation.GmallCache)
//    public Object GmallCacheMethod(ProceedingJoinPoint point) throws Throwable {
//
//        Object obj;
//
//        // 获取方法签名
//        MethodSignature signature = (MethodSignature) point.getSignature();
//        // 获取注解对象
//        GmallCache gmallCache = signature.getMethod().getAnnotation(GmallCache.class);
//        // 获取参数
//        Object[] args = point.getArgs();
//        // 封装 dataKey
//        String dataKey = gmallCache.front() + Arrays.toString(args) + gmallCache.next();
//        // 获取 缓存中的 key
//        String keyStr = (String) redisTemplate.opsForValue().get(dataKey);
//        // 缓存中有key，查询缓存并返回
//        if ( !StringUtils.isEmpty(keyStr) ){
//            return JSON.parseObject(keyStr, signature.getReturnType());
//        }else {
//            // 缓存中没有key，查询数据库并返回
//            RLock lock = redissonClient.getLock(gmallCache.front() + ":key");
//            boolean flag = lock.tryLock(RedisConst.SKULOCK_EXPIRE_PX1, RedisConst.SKULOCK_EXPIRE_PX2, TimeUnit.SECONDS);
//            if (flag){
//                // 调proceed执行切点方法
//                obj = point.proceed();
//                if (obj == null){
//                    obj = new Object();
//                }
//                redisTemplate.opsForValue().set(dataKey,obj,RedisConst.SKUKEY_TEMPORARY_TIMEOUT,TimeUnit.SECONDS);
//                return obj;
//            }else {
//                Thread.sleep(200);
//                this.GmallCacheMethod(point);
//            }
//        }
//        return point.proceed(point.getArgs());
//    }
//}
