package xianzhan.pascal.intermediate;

/**
 * The framework interface that represents the intermediate code.
 *
 * @author xianzhan
 * @since 2019-05-08
 */
public interface ICode {
    /**
     * Set and return the root node.
     *
     * @param node the node to set as root.
     * @return the root node.
     */
    ICodeNode setRoot(ICodeNode node);

    /**
     * Get the root node.
     *
     * @return the root node.
     */
    ICodeNode getRoot();
}
