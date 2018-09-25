package xianzhan.springcloud.config.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
/// import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/// @EnableEurekaServer
/**
 * @author xianzhan
 * @since 2018-09-24
 */
@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class);
    }
}


