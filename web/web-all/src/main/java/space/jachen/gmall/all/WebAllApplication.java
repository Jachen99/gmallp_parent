package space.jachen.gmall.all;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * WebAll启动器
 *
 * @author JaChen
 * @date 2023/3/9
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan({"space.jachen.gmall"})
@EnableFeignClients(basePackages= {"space.jachen.gmall"})
public class WebAllApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebAllApplication.class, args);
    }
}
