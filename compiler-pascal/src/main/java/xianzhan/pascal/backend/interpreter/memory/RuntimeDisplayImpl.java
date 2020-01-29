package xianzhan.pascal.backend.interpreter.memory;

import xianzhan.pascal.backend.interpreter.ActivationRecord;
import xianzhan.pascal.backend.interpreter.RuntimeDisplay;

import java.util.ArrayList;

/**
 * The interpreter's runtime display.
 *
 * @author xianzhan
 * @since 2019-07-02
 */
public class RuntimeDisplayImpl extends ArrayList<ActivationRecord> implements RuntimeDisplay {

    /**
     * Constructor
     */
    public RuntimeDisplayImpl() {
        // dummy element 0 (never used)
        add(null);
    }

    /**
     * Get the activation record at a given nesting level.
     *
     * @param nestingLevel the nesting level.
     * @return the activation record.
     */
    @Override
    public ActivationRecord getActivationRecord(int nestingLevel) {
        return get(nestingLevel);
    }

    /**
     * Update the display for a call to a routine at a given nesting level.
     *
     * @param nestingLevel the nesting level.
     * @param ar           the activation record for the routine.
     */
    @Override
    public void callUpdate(int nestingLevel, ActivationRecord ar) {
        // Next higher nesting level: Append a new element at the top.
        if (nestingLevel >= size()) {
            add(ar);
        }

        // Existing nesting level: Set at the specified level.
        else {
            ActivationRecord prevAr = get(nestingLevel);
            set(nestingLevel, ar.makeLinkTo(prevAr));
        }
    }

    /**
     * Update the display for a return from a routine at a given nesting level.
     *
     * @param nestingLevel the nesting level.
     */
    @Override
    public void returnUpdate(int nestingLevel) {
        int topIndex = size() - 1;
        // AR about to be popped off
        ActivationRecord ar = get(nestingLevel);
        // previous AR it points to
        ActivationRecord prevAr = ar.linkedTo();

        // Point the element at that nesting level to the
        // previous activation record.
        if (prevAr != null) {
            set(nestingLevel, prevAr);
        }

        // The top element has become null, so remove it.
        else if (nestingLevel == topIndex) {
            remove(topIndex);
        }
    }
}
