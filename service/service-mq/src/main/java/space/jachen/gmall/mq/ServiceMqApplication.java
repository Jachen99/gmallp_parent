package space.jachen.gmall.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 消息中间件rabbitMQ测试模块
 *
 * @author JaChen
 * @date 2023/3/21
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan({"space.jachen.gmall"})
public class ServiceMqApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceMqApplication.class, args);
    }
}
