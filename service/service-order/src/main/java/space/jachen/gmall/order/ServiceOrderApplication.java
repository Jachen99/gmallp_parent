package space.jachen.gmall.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 订单服务启动器
 *
 * @author JaChen
 * @date 2023/3/19
 */
@SpringBootApplication
@ComponentScan(basePackages = "space.jachen.gmall")
@EnableFeignClients(basePackages = "space.jachen.gmall")
public class ServiceOrderApplication {
    public static void main(String[] args) {

        SpringApplication.run(ServiceOrderApplication.class,args);
    }
}
