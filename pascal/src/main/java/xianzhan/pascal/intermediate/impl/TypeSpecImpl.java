package xianzhan.pascal.intermediate.impl;

import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.TypeForm;
import xianzhan.pascal.intermediate.TypeKey;
import xianzhan.pascal.intermediate.TypeSpec;

import java.util.HashMap;

/**
 * A Pascal type specification implementation.
 *
 * @author xianzhan
 * @since 2019-06-20
 */
public class TypeSpecImpl extends HashMap<TypeKey, Object> implements TypeSpec {

    /**
     * type form
     */
    private TypeForm    form;
    /**
     * type identifier
     */
    private SymTabEntry identifier;

    /**
     * Constructor.
     *
     * @param form the type form.
     */
    public TypeSpecImpl(TypeForm form) {
        this.form = form;
        this.identifier = null;
    }

    /**
     * Constructor.
     *
     * @param value a string value.
     */
    public TypeSpecImpl(String value) {
        this.form = TypeFormEnumImpl.ARRAY;

        TypeSpec indexType = new TypeSpecImpl(TypeFormEnumImpl.SUBRANGE);
        indexType.setAttribute(TypeKeyEnumImpl.SUBRANGE_BASE_TYPE, Predefined.integerType);
        indexType.setAttribute(TypeKeyEnumImpl.SUBRANGE_MIN_VALUE, 1);
        indexType.setAttribute(TypeKeyEnumImpl.SUBRANGE_MAX_VALUE, value.length());

        setAttribute(TypeKeyEnumImpl.ARRAY_INDEX_TYPE, indexType);
        setAttribute(TypeKeyEnumImpl.ARRAY_ELEMENT_TYPE, Predefined.charType);
        setAttribute(TypeKeyEnumImpl.ARRAY_ELEMENT_COUNT, value.length());
    }

    @Override
    public TypeForm getForm() {
        return form;
    }

    @Override
    public void setIdentifier(SymTabEntry identifier) {
        this.identifier = identifier;
    }

    @Override
    public SymTabEntry getIdentifier() {
        return identifier;
    }

    @Override
    public void setAttribute(TypeKey key, Object value) {
        this.put(key, value);
    }

    @Override
    public Object getAttribute(TypeKey key) {
        return this.get(key);
    }

    @Override
    public boolean isPascalString() {
        if (form != TypeFormEnumImpl.ARRAY) {
            return false;
        }

        TypeSpec elementType = (TypeSpec) getAttribute(TypeKeyEnumImpl.ARRAY_ELEMENT_TYPE);
        TypeSpec indexType = (TypeSpec) getAttribute(TypeKeyEnumImpl.ARRAY_INDEX_TYPE);

        return elementType.baseType() == Predefined.charType
                && indexType.baseType() == Predefined.integerType;
    }

    @Override
    public TypeSpec baseType() {
        return form == TypeFormEnumImpl.SUBRANGE
                ? (TypeSpec) getAttribute(TypeKeyEnumImpl.SUBRANGE_BASE_TYPE)
                : this;
    }
}
