package xianzhan.pascal.intermediate;

import xianzhan.pascal.intermediate.impl.TypeSpecImpl;

/**
 * A factory for creating type specifications.
 *
 * @author xianzhan
 * @since 2019-06-20
 */
public class TypeFactory {

    /**
     * Create a type specification of a given form.
     *
     * @param form the form.
     * @return the type specification.
     */
    public static TypeSpec createType(TypeForm form) {
        return new TypeSpecImpl(form);
    }

    /**
     * Create a string type specification.
     *
     * @param value the string value.
     * @return the type specification.
     */
    public static TypeSpec createStringType(String value) {
        return new TypeSpecImpl(value);
    }
}
