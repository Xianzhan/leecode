package architecture.service;

import java.util.List;
import java.util.Map;

import architecture.annotation.Service;
import architecture.annotation.Transaction;
import architecture.bean.FileParam;
import architecture.helper.DatabaseHelper;
import architecture.helper.UploadHelper;
import architecture.model.Customer;

/**
 * 提供客户数据服务
 * 
 * @author Lee
 *
 */
@Service
public class CustomerService {

	/**
	 * 获取客户列表
	 * 
	 * @return
	 */
	public List<Customer> getCustomerList() {
		String sql = "SELECT * FROM customer";
		return DatabaseHelper.queryEntityList(Customer.class, sql);
	}

	/**
	 * 根据 id 获取客户
	 * 
	 * @param id
	 * @return
	 */
	public Customer getCustomer(long id) {
		String sql = "SELECT * FROM customer WHERE id = ?";
		return DatabaseHelper.queryEntity(Customer.class, sql, id);
	}

	/**
	 * 创建客户
	 * 
	 * @param fieldMap
	 * @return
	 */
	@Transaction
	public boolean createCustomer(Map<String, Object> fieldMap) {
		return DatabaseHelper.insertEntity(Customer.class, fieldMap);
	}

	/**
	 * 更新客户
	 * 
	 * @param id
	 * @param fieldMap
	 * @return
	 */
	@Transaction
	public boolean updateCustomer(long id, Map<String, Object> fieldMap) {
		return DatabaseHelper.updateEntity(Customer.class, id, fieldMap);
	}

	/**
	 * 删除客户
	 * 
	 * @param id
	 * @return
	 */
	@Transaction
	public boolean deleteCustomer(long id) {
		return DatabaseHelper.deleteEntity(Customer.class, id);
	}

	/**
	 * 创建客户
	 * 
	 * @param fieldMap
	 * @param fileParam
	 * @return
	 */
	public boolean createCustomer(Map<String, Object> fieldMap,
			FileParam fileParam) {
		boolean result = DatabaseHelper.insertEntity(Customer.class, fieldMap);
		if (result) {
			UploadHelper.uploadFile("/tmp/upload/", fileParam);
		}
		return result;
	}
}
