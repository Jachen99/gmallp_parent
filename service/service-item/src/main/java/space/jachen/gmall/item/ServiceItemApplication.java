package space.jachen.gmall.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 商品详情微服务启动器
 *
 * @author JaChen
 * @date 2023/3/7
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class ServiceItemApplication  {
    public static void main(String[] args) {

        SpringApplication.run(ServiceItemApplication.class);
    }
}