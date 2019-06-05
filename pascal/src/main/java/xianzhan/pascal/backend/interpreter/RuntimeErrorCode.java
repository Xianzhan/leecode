package xianzhan.pascal.backend.interpreter;

/**
 * Runtime error codes.
 *
 * @author xianzhan
 * @since 2019-06-05
 */
public enum RuntimeErrorCode {
    //
    UNINITIALIZED_VALUE("Uninitialized value"),
    VALUE_RANGE("Value out of range"),
    INVALID_CASE_EXPRESSION_VALUE("Invalid CASE expression value"),
    DIVISION_BY_ZERO("Division by zero"),
    INVALID_STANDARD_FUNCTION_ARGUMENT("Invalid standard function argument"),
    INVALID_INPUT("Invalid input"),
    STACK_OVERFLOW("Runtime stack overflow"),
    UNIMPLEMENTED_FEATURE("Unimplemented runtime feature");

    /**
     * error message.
     */
    private String message;

    RuntimeErrorCode(String message) {
        this.message = message;
    }


    @Override
    public String toString() {
        return message;
    }
}
