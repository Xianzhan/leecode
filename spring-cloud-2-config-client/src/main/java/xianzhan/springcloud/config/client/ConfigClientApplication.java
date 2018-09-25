package xianzhan.springcloud.config.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RefreshScope 注解，需要访问 /refresh 刷新已更新的配置
 *
 * @author xianzhan
 * @since 2018-09-25
 */
@SpringBootApplication
@RestController
@RefreshScope
public class ConfigClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigClientApplication.class);
    }

    @Value("${age}")
    String age;

    @GetMapping("/age")
    public String version() {
        return age;
    }
}
