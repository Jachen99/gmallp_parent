package space.jachen.gmall.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 用户登录服务启动器
 *
 * @author JaChen
 * @date 2023/3/17
 */
@SpringBootApplication
@ComponentScan("space.jachen.gmall")
@EnableFeignClients("space.jachen.gmall")
public class ServiceUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUserApplication.class, args);
    }

}