package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.TokenType;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.Definition;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.TypeFactory;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.DefinitionEnumImpl;
import xianzhan.pascal.intermediate.impl.Predefined;
import xianzhan.pascal.intermediate.impl.SymTabKeyImpl;

import java.util.EnumSet;

/**
 * Parse Pascal constant definitions.
 *
 * @author xianzhan
 * @since 2019-06-20
 */
public class ConstantDefinitionsParser extends DeclarationsParser {

    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public ConstantDefinitionsParser(PascalParserTD parent) {
        super(parent);
    }

    /**
     * Synchronization set for a constant identifier.
     */
    private static final EnumSet<PascalTokenType> IDENTIFIER_SET =
            DeclarationsParser.TYPE_START_SET.clone();

    static {
        IDENTIFIER_SET.add(PascalTokenType.IDENTIFIER);
    }

    /**
     * Synchronization set for starting a constant.
     */
    static final EnumSet<PascalTokenType> CONSTANT_START_SET =
            EnumSet.of(
                    PascalTokenType.IDENTIFIER,
                    PascalTokenType.INTEGER,
                    PascalTokenType.REAL,
                    PascalTokenType.PLUS,
                    PascalTokenType.MINUS,
                    PascalTokenType.STRING,
                    PascalTokenType.SEMICOLON
            );

    /**
     * Synchronization set for the = token.
     */
    private static final EnumSet<PascalTokenType> EQUALS_SET =
            CONSTANT_START_SET.clone();

    static {
        EQUALS_SET.add(PascalTokenType.EQUALS);
        EQUALS_SET.add(PascalTokenType.SEMICOLON);
    }

    /**
     * Synchronization set for the start of the next definition or declaration.
     */
    private static final EnumSet<PascalTokenType> NEXT_START_SET =
            DeclarationsParser.TYPE_START_SET.clone();

    static {
        NEXT_START_SET.add(PascalTokenType.SEMICOLON);
        NEXT_START_SET.add(PascalTokenType.IDENTIFIER);
    }

    /**
     * Parse constant definitions.
     *
     * @param token    the initial token.
     * @param parentId the symbol table entry of the parent routine's name.
     * @return null
     * @throws Exception if an error occurred.
     */
    @Override
    public SymTabEntry parse(Token token, SymTabEntry parentId) throws Exception {
        token = synchronize(IDENTIFIER_SET);

        // Loop to parse a sequence of constant definitions
        // separated by semicolons.
        while (token.getType() == PascalTokenType.IDENTIFIER) {
            String name = token.getText().toLowerCase();
            SymTabEntry constantId = symTabStack.lookupLocal(name);

            // Enter the new identifier into the symbol table
            // but don't set how it's defined yet.
            if (constantId == null) {
                constantId = symTabStack.enterLocal(name);
                constantId.appendLineNumber(token.getLineNumber());
            } else {
                errorHandler.flag(token, PascalErrorCode.IDENTIFIER_REDEFINED, this);
                constantId = null;
            }

            // consume the identifier token
            token = nextToken();

            // Synchronize on the = token.
            token = synchronize(EQUALS_SET);
            if (token.getType() == PascalTokenType.EQUALS) {
                // consume the =
                token = nextToken();
            } else {
                errorHandler.flag(token, PascalErrorCode.MISSING_EQUALS, this);
            }

            // Parse the constant value.
            Token constantToken = token;
            Object value = parseConstant(token);

            // Set identifier to be a constant and set its value.
            if (constantId != null) {
                constantId.setDefinition(DefinitionEnumImpl.CONSTANT);
                constantId.setAttribute(SymTabKeyImpl.CONSTANT_VALUE, value);

                // Set the constant's type.
                TypeSpec constantType = constantToken.getType() == PascalTokenType.IDENTIFIER
                        ? getConstantType(constantToken)
                        : getConstantType(value);
                constantId.setTypeSpec(constantType);
            }

            token = currentToken();
            TokenType tokenType = token.getType();

            // Look for one or more semicolons after a definition.
            if (tokenType == PascalTokenType.SEMICOLON) {
                while (token.getType() == PascalTokenType.SEMICOLON) {
                    // consume the ;
                    token = nextToken();
                }
            }

            // If at the start of the next definition or declaration,
            // then missing a semicolon.
            else if (NEXT_START_SET.contains(tokenType)) {
                errorHandler.flag(token, PascalErrorCode.MISSING_SEMICOLON, this);
            }

            token = synchronize(IDENTIFIER_SET);
        }

        return null;
    }

    /**
     * Parse a constant value.
     *
     * @param token the current token.
     * @return the constant value.
     * @throws Exception if an error occurred.
     */
    protected Object parseConstant(Token token) throws Exception {
        TokenType sign = null;

        // Synchronize at the start of a constant.
        token = synchronize(CONSTANT_START_SET);
        TokenType tokenType = token.getType();

        // Plus or minus sign?
        if ((tokenType == PascalTokenType.PLUS) || (tokenType == PascalTokenType.MINUS)) {
            sign = tokenType;
            // consume sign
            token = nextToken();
        }

        // Parse the constant.
        switch ((PascalTokenType) token.getType()) {

            case IDENTIFIER: {
                return parseIdentifierConstant(token, sign);
            }

            case INTEGER: {
                Integer value = (Integer) token.getValue();
                // consume the number
                nextToken();
                return sign == PascalTokenType.MINUS ? -value : value;
            }

            case REAL: {
                Float value = (Float) token.getValue();
                nextToken();
                // consume the number
                return sign == PascalTokenType.MINUS ? -value : value;
            }

            case STRING: {
                if (sign != null) {
                    errorHandler.flag(token, PascalErrorCode.INVALID_CONSTANT, this);
                }

                nextToken();
                // consume the string
                return token.getValue();
            }

            default: {
                errorHandler.flag(token, PascalErrorCode.INVALID_CONSTANT, this);
                return null;
            }
        }
    }

    /**
     * Parse an identifier constant.
     *
     * @param token the current token.
     * @param sign  the sign, if any.
     * @return the constant value.
     * @throws Exception if an error occurred.
     */
    protected Object parseIdentifierConstant(Token token, TokenType sign) throws Exception {
        String name = token.getText().toLowerCase();
        SymTabEntry id = symTabStack.lookup(name);

        nextToken();  // consume the identifier

        // The identifier must have already been defined
        // as an constant identifier.
        if (id == null) {
            errorHandler.flag(token, PascalErrorCode.IDENTIFIER_UNDEFINED, this);
            return null;
        }

        Definition definition = id.getDefinition();

        if (definition == DefinitionEnumImpl.CONSTANT) {
            Object value = id.getAttribute(SymTabKeyImpl.CONSTANT_VALUE);
            id.appendLineNumber(token.getLineNumber());

            if (value instanceof Integer) {
                return sign == PascalTokenType.MINUS ? -((Integer) value) : value;
            } else if (value instanceof Float) {
                return sign == PascalTokenType.MINUS ? -((Float) value) : value;
            } else if (value instanceof String) {
                if (sign != null) {
                    errorHandler.flag(token, PascalErrorCode.INVALID_CONSTANT, this);
                }

                return value;
            } else {
                return null;
            }
        } else if (definition == DefinitionEnumImpl.ENUMERATION_CONSTANT) {
            Object value = id.getAttribute(SymTabKeyImpl.CONSTANT_VALUE);
            id.appendLineNumber(token.getLineNumber());

            if (sign != null) {
                errorHandler.flag(token, PascalErrorCode.INVALID_CONSTANT, this);
            }

            return value;
        } else if (definition == null) {
            errorHandler.flag(token, PascalErrorCode.NOT_CONSTANT_IDENTIFIER, this);
            return null;
        } else {
            errorHandler.flag(token, PascalErrorCode.INVALID_CONSTANT, this);
            return null;
        }
    }

    /**
     * Return the type of a constant given its value.
     *
     * @param value the constant value
     * @return the type specification.
     */
    protected TypeSpec getConstantType(Object value) {
        TypeSpec constantType = null;

        if (value instanceof Integer) {
            constantType = Predefined.integerType;
        } else if (value instanceof Float) {
            constantType = Predefined.realType;
        } else if (value instanceof String) {
            if (((String) value).length() == 1) {
                constantType = Predefined.charType;
            } else {
                constantType = TypeFactory.createStringType((String) value);
            }
        }

        return constantType;
    }

    /**
     * Return the type of a constant given its identifier.
     *
     * @param identifier the constant's identifier.
     * @return the type specification.
     */
    protected TypeSpec getConstantType(Token identifier) {
        String name = identifier.getText().toLowerCase();
        SymTabEntry id = symTabStack.lookup(name);

        if (id == null) {
            return null;
        }

        Definition definition = id.getDefinition();

        if ((definition == DefinitionEnumImpl.CONSTANT) || (definition == DefinitionEnumImpl.ENUMERATION_CONSTANT)) {
            return id.getTypeSpec();
        } else {
            return null;
        }
    }
}
