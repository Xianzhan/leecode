package xianzhan.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @auther xianzhan
 * @sinese 2018-04-26
 */
@Configuration
@ConfigurationProperties(prefix = "xianzhan")
@PropertySource("classpath:resource.properties")
public class Resource {
    private String language;
    private String github;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }
}
