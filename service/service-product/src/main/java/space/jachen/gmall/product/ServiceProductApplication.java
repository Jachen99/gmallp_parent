package space.jachen.gmall.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 商品服务启动类
 *
 * @author JaChen
 * @date 2023/03/03
 */
@SpringBootApplication
@ComponentScan(basePackages = "space.jachen.gmall")
public class ServiceProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceProductApplication.class);
    }
}