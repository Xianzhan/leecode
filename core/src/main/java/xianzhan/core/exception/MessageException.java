package xianzhan.core.exception;

/**
 * 消息异常
 *
 * @author xianzhan
 * @since 2019-02-06
 */
public class MessageException extends RuntimeException {

    private static final long serialVersionUID = 5329164196332842964L;

    public MessageException(String message) {
        super(message);
    }

    /**
     * 该父类方法当抛出异常时，会访问当前线程的栈帧，然后记录所有操作，会比较消耗资源。
     * 现重写该方法，消息异常只为传递消息
     *
     * @return this
     */
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
