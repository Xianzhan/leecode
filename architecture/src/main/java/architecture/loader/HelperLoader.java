package architecture.loader;

import architecture.helper.AopHelper;
import architecture.helper.BeanHelper;
import architecture.helper.ClassHelper;
import architecture.helper.ControllerHelper;
import architecture.helper.IoCHelper;
import architecture.util.ClassUtil;

/**
 * 加载相应的 Helper 类
 * 
 * @author Lee
 *
 */
public final class HelperLoader {

	public static void init() {
		Class<?>[] classList = {
				ClassHelper.class,
				BeanHelper.class,
				// AopHelper 要在 IoCHelper 之前加载
				// 先通过 AopHelper 获取代理对象，
				// 然后才能通过 IoCHelper 进行依赖注入
				AopHelper.class,
				IoCHelper.class,
				ControllerHelper.class
		};
		for (Class<?> cls : classList) {
			ClassUtil.loadClass(cls.getName(), true);
		}
	}
}
