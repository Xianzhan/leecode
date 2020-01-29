package xianzhan.pascal.intermediate;

import java.util.ArrayList;

/**
 * The interface for a node of the intermediate code.
 *
 * @author xianzhan
 * @since 2019-06-03
 */
public interface ICodeNode {

    /**
     * Return the node type
     *
     * @return the node type
     */
    ICodeNodeType getType();

    /**
     * Return the parent of this node.
     *
     * @return the parent node.
     */
    ICodeNode getParent();

    /**
     * Add a child node.
     *
     * @param node the child node.
     * @return the child node.
     */
    ICodeNode addChild(ICodeNode node);

    /**
     * Return an array list of this node's children.
     *
     * @return the array list of children.
     */
    ArrayList<ICodeNode> getChildren();

    /**
     * Set a node attribute.
     *
     * @param key   the attribute key.
     * @param value the attribute value.
     */
    void setAttribute(ICodeKey key, Object value);

    /**
     * Get the value of a node attribute.
     *
     * @param key the attribute key.
     * @return the attribute value.
     */
    Object getAttribute(ICodeKey key);

    /**
     * Make a copy of this node.
     *
     * @return the copy.
     */
    ICodeNode copy();

    /**
     * Set the type specification of this node.
     *
     * @param typeSpec the type sprcification to set.
     */
    void setTypeSpec(TypeSpec typeSpec);

    /**
     * Return the type specification of this node.
     *
     * @return the type specification.
     */
    TypeSpec getTypeSpec();
}
