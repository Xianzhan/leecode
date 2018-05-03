package xianzhan.spring;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author xianzhan
 * @since 2018-05-03
 */
public class ResourceTest {

    @Test
    public void testClassPathResource() throws IOException {
        Resource resource = new ClassPathResource("/templates/thymeleaf/index.html");
        System.out.println(resource);
        InputStream is = resource.getInputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        int available = bis.available();
        byte[] resourceBytes = new byte[available];
        int read = bis.read(resourceBytes, 0, available);
        System.out.println(read);
        String resourceString = new String(resourceBytes);
        System.out.println(resourceString);
    }
}
