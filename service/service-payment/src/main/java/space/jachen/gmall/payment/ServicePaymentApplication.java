package space.jachen.gmall.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 支付模块启动类
 *
 * @author JaChen
 * @date 2023/3/22
 */
@SpringBootApplication
@EnableFeignClients(basePackages= {"space.jachen.gmall"})
@ComponentScan(basePackages = {"space.jachen.gmall"})
public class ServicePaymentApplication  {
    public static void main(String[] args) {
        SpringApplication.run(ServicePaymentApplication.class,args);
    }
}