package xianzhan.pojo.vo;

/**
 * @author xianzhan
 * @since 2018-04-18
 */
public class ResultVO {
    private int    state;
    private String message;
    private Object data;

    private ResultVO() {
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

    public ResultVO setMessage(String message) {
        this.state = 500;
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public ResultVO setData(Object data) {
        this.data = data;
        return this;
    }

    public static ResultVO ok() {
        return new ResultVO();
    }

    public static ResultVO ok(Object data) {
        return new ResultVO().setData(data);
    }

    public static ResultVO err(String message) {
        return new ResultVO().setMessage(message);
    }
}
