package xianzhan.pascal.backend.interpreter;

import java.util.ArrayList;

/**
 * Interface for the interpreter's runtime stack.
 *
 * @author xianzhan
 * @since 2019-07-02
 */
public interface RuntimeStack {

    /**
     * Return an array list of the activation records on the stack.
     *
     * @return an array list of the activation records on the stack.
     */
    ArrayList<ActivationRecord> records();

    /**
     * Get the topmost activation record at a given nesting level.
     *
     * @param nestingLevel the nesting level.
     * @return the activation record.
     */
    ActivationRecord getTopmost(int nestingLevel);

    /**
     * Return the current nesting level.
     *
     * @return the current nesting level.
     */
    int currentNestingLevel();

    /**
     * Pop an activation record off the stack.
     */
    void pop();

    /**
     * Push an activation record onto the stack.
     *
     * @param ar the activation record to push.
     */
    void push(ActivationRecord ar);
}
