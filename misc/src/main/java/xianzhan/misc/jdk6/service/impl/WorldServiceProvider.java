package xianzhan.misc.jdk6.service.impl;

import xianzhan.misc.jdk6.service.ServiceProvider;

/**
 * 描述：服务实现
 *
 * @author Lee
 * @since 2019-04-19
 */
public class WorldServiceProvider implements ServiceProvider {

    @Override
    public String getMessage() {
        return "World";
    }
}
