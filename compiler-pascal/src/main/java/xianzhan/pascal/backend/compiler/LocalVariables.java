package xianzhan.pascal.backend.compiler;

import java.util.ArrayList;

/**
 * Maintain a method's local variables array.
 *
 * @author xianzhan
 * @since 2019-07-20
 */
public class LocalVariables {

    /**
     * List of boolean to keep track of reserved local variables.
     * The ith element is true if the ith variable is being used,
     * else it is false. The final size of the list is the total number
     * of local variables used by the method.
     */
    private ArrayList<Boolean> reserved;

    public LocalVariables(int index) {
        reserved = new ArrayList<>();
        for (int i = 0; i <= index; i++) {
            reserved.add(Boolean.TRUE);
        }
    }

    /**
     * Reserve a local variable.
     *
     * @return the index of the newly reserved variable.
     */
    public int reserve() {
        // Search for existing but unreserved local variables.
        for (int i = 0; i < reserved.size(); i++) {
            if (!reserved.get(i)) {
                reserved.set(i, Boolean.TRUE);
                return i;
            }
        }

        // Reserved a new variable.
        reserved.add(Boolean.TRUE);
        return reserved.size() + 1;
    }

    /**
     * Release a local variable that's no longer needed.
     *
     * @param index the index of the variable.
     */
    public void release(int index) {
        reserved.set(index, Boolean.FALSE);
    }

    /**
     * Return the count of local variables needed by the method.
     *
     * @return the count.
     */
    public int count() {
        return reserved.size();
    }
}
