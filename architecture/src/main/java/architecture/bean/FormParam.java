package architecture.bean;

/**
 * 封装表单参数
 * 
 * @author Lee
 *
 */
public class FormParam {

	private String fieldName;
	private Object fieldValue;

	public FormParam(String fieldName, Object fieldValue) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Object getFieldValue() {
		return fieldValue;
	}

}
