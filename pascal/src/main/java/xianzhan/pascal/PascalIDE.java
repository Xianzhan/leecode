package xianzhan.pascal;

import xianzhan.pascal.ide.IDEFrame;

/**
 * The simple Pascal Integrated Development Environment.
 *
 * @author xianzhan
 * @since 2019-07-10
 */
public class PascalIDE {

    public PascalIDE() {
        new IDEFrame();
    }

    public static void main(String[] args) {
        new PascalIDE();
    }
}
