package xianzhan.pascal.intermediate;

import xianzhan.pascal.intermediate.impl.ICodeImpl;
import xianzhan.pascal.intermediate.impl.ICodeNodeImpl;

/**
 * A factory for creating objects that implement the intermediate code.
 *
 * @author xianzhan
 * @since 2019-06-03
 */
public class ICodeFactory {

    public static ICode createICode() {
        return new ICodeImpl();
    }

    public static ICodeNode createICodeNode(ICodeNodeType type) {
        return new ICodeNodeImpl(type);
    }
}
