package space.jachen.gmall.activity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 秒杀服务启动器
 *
 * @author JaChen
 * @date 2023/03/24
 */
@SpringBootApplication
@ComponentScan(basePackages = "space.jachen.gmall")
@EnableFeignClients(basePackages = "space.jachen.gmall")
public class ServiceActivityApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceActivityApplication.class, args);
    }
}