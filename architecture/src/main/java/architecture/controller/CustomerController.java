package architecture.controller;

import java.util.List;
import java.util.Map;

import architecture.annotation.Action;
import architecture.annotation.Controller;
import architecture.annotation.Inject;
import architecture.bean.Data;
import architecture.bean.FileParam;
import architecture.bean.Param;
import architecture.bean.View;
import architecture.model.Customer;
import architecture.service.CustomerService;

@Controller
public class CustomerController {

	@Inject
	private CustomerService customerService;

	/**
	 * 进入 客户列表 界面
	 * 
	 * @param param
	 * @return
	 */
	@Action("get:/customer")
	public View index(Param param) {
		List<Customer> customerList = customerService.getCustomerList();
		return new View("customer.jsp").addModel("customerList", customerList);
	}

	/**
	 * 处理 创建客户 请求
	 * 
	 * @param param
	 * @return
	 */
	@Action("post:/customer_create")
	public Data createSubmit(Param param) {
		Map<String, Object> fieldMap = param.getFieldMap();
		FileParam fileParam = param.getFile("photo");
		boolean result = customerService.createCustomer(fieldMap, fileParam);
		return new Data(result);
	}
}
