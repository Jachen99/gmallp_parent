package space.jachen.gmall.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * @author JaChen
 * @date 2023/3/13 16:34
 */
@Component
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor executor(){

        return new ThreadPoolExecutor(
                30, 1000, 30,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(200),
                Executors.defaultThreadFactory(),
                (r, executor) -> {
                    System.out.println("自定义拒绝策略");
                    System.out.println("r = " + r);
                    System.out.println("executor = " + executor);
                }
        );
    }
}
