package xianzhan.pascal;

/**
 * Pascal Runtime Library:
 * Compute and print the elapsed run time of a compiled Pascal program.
 *
 * @author xianzhan
 * @since 2019-07-21
 */
public class RunTimer {
    long startTime;

    /**
     * Constructor.
     */
    public RunTimer() {
        startTime = System.currentTimeMillis();
    }

    /**
     * Compute and print the elapsed run time.
     */
    public void printElapsedTime() {
        float elapsedTime = (System.currentTimeMillis() - startTime) / 1000F;
        System.out.println(String.format("\n%,20.2f seconds total execution time.", elapsedTime));
    }
}
