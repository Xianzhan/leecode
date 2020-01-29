package xianzhan.pascal.intermediate.impl;

import xianzhan.pascal.intermediate.ICodeFactory;
import xianzhan.pascal.intermediate.ICodeKey;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.ICodeNodeType;
import xianzhan.pascal.intermediate.TypeSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * An implementation of a node of the intermediate code.
 *
 * @author xianzhan
 * @since 2019-06-03
 */
public class ICodeNodeImpl extends HashMap<ICodeKey, Object> implements ICodeNode {

    /**
     * node type.
     */
    private ICodeNodeType        type;
    /**
     * parent node
     */
    private ICodeNode            parent;
    /**
     * children array list.
     */
    private ArrayList<ICodeNode> children;
    /**
     * data type specification
     */
    private TypeSpec             typeSpec;

    /**
     * Constructor.
     *
     * @param type
     */
    public ICodeNodeImpl(ICodeNodeType type) {
        this.type = type;
        this.parent = null;
        this.children = new ArrayList<>();
    }

    @Override
    public ICodeNodeType getType() {
        return type;
    }

    @Override
    public ICodeNode getParent() {
        return parent;
    }

    @Override
    public ICodeNode addChild(ICodeNode node) {
        if (node != null) {
            children.add(node);
            ((ICodeNodeImpl) node).parent = this;
        }
        return node;
    }

    @Override
    public ArrayList<ICodeNode> getChildren() {
        return children;
    }

    @Override
    public void setAttribute(ICodeKey key, Object value) {
        put(key, value);
    }

    @Override
    public Object getAttribute(ICodeKey key) {
        return get(key);
    }

    @Override
    public ICodeNode copy() {
        // Create a copy with the same type.
        ICodeNodeImpl copy = (ICodeNodeImpl) ICodeFactory.createICodeNode(type);

        Set<Entry<ICodeKey, Object>> attributes = entrySet();

        // Copy attributes
        for (Entry<ICodeKey, Object> attribute : attributes) {
            copy.put(attribute.getKey(), attribute.getValue());
        }
        return copy;
    }

    @Override
    public void setTypeSpec(TypeSpec typeSpec) {
        this.typeSpec = typeSpec;
    }

    @Override
    public TypeSpec getTypeSpec() {
        return typeSpec;
    }

    @Override
    public String toString() {
        return type.toString();
    }
}
