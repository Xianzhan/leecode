package xianzhan.misc.jdk6;

import xianzhan.misc.jdk6.service.ServiceProvider;
import xianzhan.misc.jdk6.service.impl.HelloServiceProvider;
import xianzhan.misc.jdk6.service.impl.WorldServiceProvider;

import java.util.ServiceLoader;

/**
 * 描述：ServiceLoader 使用片段
 *
 * @author Lee
 * @see ServiceProvider
 * @see HelloServiceProvider
 * @see WorldServiceProvider
 * @since 2019-04-19
 */
public class ServiceLoaderDemo {

    public static void main(String[] args) {
        ServiceLoader<ServiceProvider> serviceProviders = ServiceLoader.load(ServiceProvider.class);
        serviceProviders.stream()
                .forEach(provider -> {
                    ServiceProvider serviceProvider = provider.get();
                    System.out.println(serviceProvider.getMessage());
                });
        // Hello
        // World
    }
}
