package space.jachen.gmall.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 定时器服务启动器
 *
 * @author JaChen
 * @date 2023/3/24
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan({"space.jachen.gmall"})
public class ServiceTaskApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceTaskApplication.class, args);
    }

}
