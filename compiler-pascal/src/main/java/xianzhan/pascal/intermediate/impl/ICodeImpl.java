package xianzhan.pascal.intermediate.impl;

import xianzhan.pascal.intermediate.ICode;
import xianzhan.pascal.intermediate.ICodeNode;

/**
 * An implementation of the intermediate code as a parse tree.
 *
 * @author xianzhan
 * @since 2019-06-03
 */
public class ICodeImpl implements ICode {

    /**
     * root node
     */
    private ICodeNode root;

    @Override
    public ICodeNode setRoot(ICodeNode node) {
        root = node;
        return root;
    }

    @Override
    public ICodeNode getRoot() {
        return root;
    }
}
