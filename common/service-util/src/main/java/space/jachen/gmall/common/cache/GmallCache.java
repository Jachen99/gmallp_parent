package space.jachen.gmall.common.cache;

import java.lang.annotation.*;

/**
 * Redis分布式缓存自定义注解
 *
 * @author jachen
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface GmallCache {

    String front() default "value:";

    String next() default ":info";

}
