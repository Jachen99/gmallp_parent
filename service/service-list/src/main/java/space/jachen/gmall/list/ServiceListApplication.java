package space.jachen.gmall.list;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * 搜索服务启动器
 *
 * @author JaChen
 * @date 2023/02/14
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan("space.jachen.gmall")
public class ServiceListApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceListApplication.class,args);
    }
}