package xianzhan.pascal;

/**
 * Pascal Runtime Library:
 * Exception thrown for an error while executing the generated code.
 *
 * @author xianzhan
 * @since 2019-07-21
 */
public class PascalRuntimeException extends Exception {
    public PascalRuntimeException(String message) {
        super(message);
    }
}
