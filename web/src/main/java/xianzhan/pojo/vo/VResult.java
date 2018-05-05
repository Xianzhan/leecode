package xianzhan.pojo.vo;

/**
 * @author xianzhan
 * @since 2018-04-18
 */
public class VResult {
    private int    state;
    private String message;
    private Object data;

    private VResult() {
        state = 200;
        message = "";
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

    public VResult setMessage(String message) {
        this.state = 500;
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public VResult setData(Object data) {
        this.data = data;
        return this;
    }

    public static VResult ok() {
        return new VResult();
    }

    public static VResult ok(Object data) {
        return new VResult().setData(data);
    }

    public static VResult err(String message) {
        return new VResult().setMessage(message);
    }
}
