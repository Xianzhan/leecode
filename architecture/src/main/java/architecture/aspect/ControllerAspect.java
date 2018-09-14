package architecture.aspect;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import architecture.annotation.Aspect;
import architecture.annotation.Controller;
import architecture.proxy.AspectProxy;

/**
 * 拦截 Controller 所有方法
 * 
 * @author Lee
 *
 */
@Aspect(Controller.class)
public class ControllerAspect extends AspectProxy {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ControllerAspect.class);

	private long begin;

	@Override
	public void before(Class<?> cls, Method method, Object[] params) {
		LOGGER.debug("---------- begin ----------");
		LOGGER.debug(String.format("class: %s", cls.getName()));
		LOGGER.debug(String.format("method: %s", method.getName()));
		begin = System.currentTimeMillis();
	}

	@Override
	public void after(Class<?> cls, Method method, Object[] params,
			Object result) {
		LOGGER.debug(String.format("time: %dms",
				System.currentTimeMillis() - begin));
		LOGGER.debug("---------- end ----------");
	}

}
