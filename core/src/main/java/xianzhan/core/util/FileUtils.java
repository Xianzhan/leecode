package xianzhan.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * 文件工具类
 *
 * @author lee
 * @since 2018-01-29
 */
public final class FileUtils {

    /**
     * 获取根文件夹下的资源
     *
     * @param fileName 文件名
     * @return Properties
     */
    private static Properties getProperties(String fileName) {
        Properties properties = new Properties();
        try {
            ClassLoader classLoader = FileUtils.class.getClassLoader();
            URL url = classLoader.getResource(fileName);

            if (url == null) {
                return properties;
            }

            URI uri = url.toURI();
            Path path = Paths.get(uri);
            InputStream is = Files.newInputStream(path);
            properties.load(is);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static void main(String[] args) {
        String fileName = "log4j.properties";
        System.out.println(getProperties(fileName).get("log4j.rootCategory"));
    }
}
