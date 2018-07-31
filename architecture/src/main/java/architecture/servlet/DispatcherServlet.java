package architecture.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import architecture.bean.Data;
import architecture.bean.Handler;
import architecture.bean.Param;
import architecture.bean.View;
import architecture.helper.BeanHelper;
import architecture.helper.ConfigHelper;
import architecture.helper.ControllerHelper;
import architecture.helper.RequestHelper;
import architecture.helper.ServletHelper;
import architecture.helper.UploadHelper;
import architecture.loader.HelperLoader;
import architecture.util.JsonUtil;
import architecture.util.ReflectionUtil;
import architecture.util.StringUtil;

/**
 * 请求转发器
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DispatcherServlet() {
	}

	public void init(ServletConfig config) throws ServletException {
		// 初始化相关 Helper 类
		HelperLoader.init();
		// 获取 ServletContext 对象（用于注册 Servlet）
		ServletContext servletContext = config.getServletContext();
		// 注册处理 JSP 的 Servlet
		ServletRegistration jspServlet = servletContext
				.getServletRegistration("jsp");
		jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");
		// 注册处理静态资源的默认 Servlet
		ServletRegistration defaultServlet = servletContext
				.getServletRegistration("default");
		defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");

		// 上传文件初始化
		UploadHelper.init(servletContext);
	}

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ServletHelper.init(request, response);
		try {

			// 获取请求方法与请求路径
			String requestMethod = request.getMethod().toLowerCase();
			String requestPath = request.getPathInfo();

			if (requestPath.equals("/favicon.ico")) {
				return;
			}

			// 获取 Action 处理器
			Handler handler = ControllerHelper.getHandler(requestMethod,
					requestPath);
			if (null != handler) {
				// 获取 Controller 类及其 Bean 实例
				Class<?> controllerClass = handler.getControllerClass();
				Object controllerBean = BeanHelper.getBean(controllerClass);

				// 判断是否有文件上传
				Param param;
				if (UploadHelper.isMultipart(request)) {
					param = UploadHelper.createParam(request);
				} else {
					param = RequestHelper.createParam(request);
				}

				// 创建请求参数对象
				// Map<String, Object> paramMap = new
				// HashedMap<>();
				// Enumeration<String> paramNames =
				// request.getParameterNames();
				// while (paramNames.hasMoreElements()) {
				// String paramName = paramNames.nextElement();
				// String paramValue =
				// request.getParameter(paramName);
				// paramMap.put(paramName, paramValue);
				// String body = CodecUtil.decodeURL(
				// StreamUtil.getString(request.getInputStream()));
				// if (StringUtil.isNotEmpty(body)) {
				// String[] params = StringUtil.splitString(body,
				// "&");
				// if (ArrayUtil.isNotEmpty(params)) {
				// for (String param : params) {
				// String[] array = StringUtil.splitString(param,
				// "=");
				// if (ArrayUtil.isNotEmpty(array)
				// && array.length == 2) {
				// paramName = array[0];
				// paramValue = array[1];
				// paramMap.put(paramName, paramValue);
				// }
				// }
				// }
				// }
				// Param param = new Param(paramMap);

				// 调用 Action 方法
				Object result;
				Method actionMethod = handler.getActionMethod();

				// 判断 param 是否为空
				if (param.isEmpty()) {
					result = ReflectionUtil.invokeMethod(controllerBean,
							actionMethod);
				} else {
					result = ReflectionUtil.invokeMethod(controllerBean,
							actionMethod, param);
				}

				// 处理 Action 方法返回值
				if (result instanceof View) {
					// 返回 JSP 页面
					// View view = (View) result;
					// String path = view.getPath();
					// if (StringUtil.isNotEmpty(path)) {
					// if (path.startsWith("/")) {
					// response.sendRedirect(
					// request.getContextPath() + path);
					// } else {
					// Map<String, Object> model =
					// view.getModel();
					// for (Map.Entry<String, Object> entity :
					// model
					// .entrySet()) {
					// request.setAttribute(entity.getKey(),
					// entity.getValue());
					// }
					// request.getRequestDispatcher(
					// ConfigHelper.getAppJspPath() + path)
					// .forward(request, response);
					// }
					// }
					handleViewResult((View) result, request, response);
				} else if (result instanceof Data) {
					// 返回 JSON 数据
					// Data data = (Data) result;
					// Object model = data.getModel();
					// if (null != model) {
					// response.setContentType("application/json");
					// response.setCharacterEncoding("UTF-8");
					// PrintWriter out = response.getWriter();
					// String json = JsonUtil.toJson(model);
					// out.write(json);
					// out.flush();
					// out.close();
					// }
					handleDataResult((Data) result, response);
				}
			}
		} finally {
			ServletHelper.destroy();
		}

	}

	private void handleDataResult(Data data, HttpServletResponse response)
			throws IOException {
		Object model = data.getModel();
		if (model != null) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			String json = JsonUtil.toJson(model);
			out.write(json);
			out.flush();
			out.close();
		}
	}

	private void handleViewResult(View view, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String path = view.getPath();
		if (StringUtil.isNotEmpty(path)) {
			if (path.startsWith("/")) {
				response.sendRedirect(request.getContextPath() + path);
			} else {
				Map<String, Object> model = view.getModel();
				for (Map.Entry<String, Object> entry : model.entrySet()) {
					request.setAttribute(entry.getKey(), entry.getValue());
				}
				request.getRequestDispatcher(
						ConfigHelper.getAppJspPath() + path)
						.forward(request, response);
			}
		}
	}
}
