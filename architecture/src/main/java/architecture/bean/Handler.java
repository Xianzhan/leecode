package architecture.bean;

import java.lang.reflect.Method;

/**
 * 封装 Action 信息
 * 
 * @author Lee
 *
 */
public class Handler {

	/**
	 * Controller 类
	 */
	private Class<?> controllerClass;

	/**
	 * Action 方法
	 */
	private Method actionMethod;

	public Handler(Class<?> controllerClass, Method actionMethod) {
		super();
		this.controllerClass = controllerClass;
		this.actionMethod = actionMethod;
	}

	public Class<?> getControllerClass() {
		return controllerClass;
	}

	public void setControllerClass(Class<?> controllerClass) {
		this.controllerClass = controllerClass;
	}

	public Method getActionMethod() {
		return actionMethod;
	}

	public void setActionMethod(Method actionMethod) {
		this.actionMethod = actionMethod;
	}

}
