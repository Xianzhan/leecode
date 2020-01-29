package xianzhan.pascal.backend.compiler;

/**
 * Maintain a method's local runtime stack.
 *
 * @author xianzhan
 * @since 2019-07-20
 */
public class LocalStack {

    /**
     * Current stack size
     */
    private int size;
    /**
     * Maximum attained stack size
     */
    private int maxSize;

    public LocalStack() {
        this.size = 0;
        this.maxSize = 0;
    }

    public int getSize() {
        return size;
    }

    /**
     * Increase the stack size by a given amount.
     *
     * @param amount the amount to increase.
     */
    public void increase(int amount) {
        size += amount;
        maxSize = Math.max(maxSize, size);
    }

    /**
     * Decrease the stack size by a given amount.
     *
     * @param amount the amount to decrease.
     */
    public void decrease(int amount) {
        size -= amount;
    }

    /**
     * Increase and decrease the stack size by the same amount.
     *
     * @param amount the amount to increase and decrease.
     */
    public void use(int amount) {
        increase(amount);
        decrease(amount);
    }

    /**
     * Return the maximum attained stack size.
     *
     * @return the maximum size.
     */
    public int capacity() {
        return maxSize;
    }
}
