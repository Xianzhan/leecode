package xianzhan.pascal.backend.interpreter.memory;

import xianzhan.pascal.backend.interpreter.ActivationRecord;
import xianzhan.pascal.backend.interpreter.MemoryFactory;
import xianzhan.pascal.backend.interpreter.RuntimeDisplay;
import xianzhan.pascal.backend.interpreter.RuntimeStack;

import java.util.ArrayList;

/**
 * The interpreter's runtime stack.
 *
 * @author xianzhan
 * @since 2019-07-02
 */
public class RuntimeStackImpl extends ArrayList<ActivationRecord> implements RuntimeStack {

    /**
     * Runtime display
     */
    private RuntimeDisplay display;

    /**
     * Constructor
     */
    public RuntimeStackImpl() {
        this.display = MemoryFactory.createRuntimeDisplay();
    }

    /**
     * @return an array list of the activation records on the stack.
     */
    @Override
    public ArrayList<ActivationRecord> records() {
        return this;
    }

    /**
     * Get the topmost activation record at a given nesting level.
     *
     * @param nestingLevel the nesting level.
     * @return the activation record.
     */
    @Override
    public ActivationRecord getTopmost(int nestingLevel) {
        return display.getActivationRecord(nestingLevel);
    }

    /**
     * @return the current nesting level.
     */
    @Override
    public int currentNestingLevel() {
        int topIndex = size() - 1;
        return topIndex >= 0
                ? get(topIndex).getNestingLevel()
                : -1;
    }

    /**
     * Pop an activation record off the stack for a returning routine.
     */
    @Override
    public void pop() {
        display.returnUpdate(currentNestingLevel());
        remove(size() - 1);
    }

    /**
     * Push an activation record onto the stack for a routine being called.
     *
     * @param ar the activation record to push.
     */
    @Override
    public void push(ActivationRecord ar) {
        int nestingLevel = ar.getNestingLevel();
        add(ar);
        display.callUpdate(nestingLevel, ar);
    }
}
