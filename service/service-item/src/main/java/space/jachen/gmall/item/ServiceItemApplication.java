package space.jachen.gmall.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 商品详情微服务启动器
 *
 * @author JaChen
 * @date 2023/3/7
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = "space.jachen.gmall")
// 需要指定feign接口需要扫描的包路径
@EnableFeignClients(basePackages = "space.jachen.gmall")
public class ServiceItemApplication  {
    public static void main(String[] args) {

        SpringApplication.run(ServiceItemApplication.class);
    }
}