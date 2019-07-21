package xianzhan.pascal.backend.compiler;

/**
 * Error during the Pascal compiler's code generation.
 *
 * @author xianzhan
 * @since 2019-07-20
 */
public class PascalCompilerException extends Exception {

    public PascalCompilerException(String message) {
        super(message);
    }
}
