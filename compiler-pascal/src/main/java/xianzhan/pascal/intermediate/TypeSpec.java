package xianzhan.pascal.intermediate;

/**
 * The interface for a type specification.
 *
 * @author xianzhan
 * @since 2019-06-20
 */
public interface TypeSpec {

    /**
     * Getter
     *
     * @return the type form.
     */
    TypeForm getForm();

    /**
     * Setter.
     *
     * @param identifier the type identifier (symbol table entry).
     */
    void setIdentifier(SymTabEntry identifier);

    /**
     * Getter.
     *
     * @return the type identifier (symbol table entry).
     */
    SymTabEntry getIdentifier();

    /**
     * Set an attribute of the specification.
     *
     * @param key   the attribute key.
     * @param value the attribute value.
     */
    void setAttribute(TypeKey key, Object value);

    /**
     * Get the value of an attribute of the specification.
     *
     * @param key the attribute key.
     * @return the attribute value.
     */
    Object getAttribute(TypeKey key);

    /**
     * return true if this is a Pascal string type.
     *
     * @return true if this is a Pascal string type.
     */
    boolean isPascalString();

    /**
     * return the base type of this type.
     *
     * @return the base type of this type.
     */
    TypeSpec baseType();
}
