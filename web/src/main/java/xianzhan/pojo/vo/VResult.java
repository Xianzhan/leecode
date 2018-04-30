package xianzhan.pojo.vo;

/**
 * @author xianzhan
 * @since 2018-04-18
 */
public class VResult {
    private int    state;
    private String message;
    private Object data;

    {
        state = 200;
        message = "";
    }

    private VResult() {

    }

    private VResult(Object data) {
        this.data = data;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static VResult ok() {
        return new VResult();
    }

    public static VResult ok(Object data) {
        return new VResult(data);
    }
}
