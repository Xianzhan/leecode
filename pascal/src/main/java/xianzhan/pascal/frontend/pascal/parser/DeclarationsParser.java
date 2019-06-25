package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.impl.DefinitionEnumImpl;

import java.util.EnumSet;

/**
 * Parse Pascal declarations.
 *
 * @author xianzhan
 * @since 2019-06-20
 */
public class DeclarationsParser extends PascalParserTD {

    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public DeclarationsParser(PascalParserTD parent) {
        super(parent);
    }

    static final EnumSet<PascalTokenType> DECLARATION_START_SET =
            EnumSet.of(
                    PascalTokenType.CONST,
                    PascalTokenType.TYPE,
                    PascalTokenType.VAR,
                    PascalTokenType.PROCEDURE,
                    PascalTokenType.FUNCTION,
                    PascalTokenType.BEGIN
            );

    static final EnumSet<PascalTokenType> TYPE_START_SET =
            DECLARATION_START_SET.clone();

    static {
        TYPE_START_SET.remove(PascalTokenType.CONST);
    }

    static final EnumSet<PascalTokenType> VAR_START_SET =
            TYPE_START_SET.clone();

    static {
        VAR_START_SET.remove(PascalTokenType.TYPE);
    }

    static final EnumSet<PascalTokenType> ROUTINE_START_SET =
            VAR_START_SET.clone();

    static {
        ROUTINE_START_SET.remove(PascalTokenType.VAR);
    }

    /**
     * Parse declarations.
     * To be overridden by the specialized declarations parser subclasses.
     *
     * @param token the initial token.
     * @throws Exception if an error occurred.
     */
    public void parse(Token token) throws Exception {
        token = synchronize(DECLARATION_START_SET);

        if (token.getType() == PascalTokenType.CONST) {
            // consume CONST
            token = nextToken();

            ConstantDefinitionsParser constantDefinitionsParser = new ConstantDefinitionsParser(this);
            constantDefinitionsParser.parse(token);
        }

        token = synchronize(TYPE_START_SET);

        if (token.getType() == PascalTokenType.TYPE) {
            // consume TYPE
            token = nextToken();

            TypeDefinitionsParser typeDefinitionsParser = new TypeDefinitionsParser(this);
            typeDefinitionsParser.parse(token);
        }

        token = synchronize(VAR_START_SET);

        if (token.getType() == PascalTokenType.VAR) {
            // consume VAR
            token = nextToken();

            VariableDeclarationsParser variableDeclarationsParser = new VariableDeclarationsParser(this);
            variableDeclarationsParser.setDefinition(DefinitionEnumImpl.VARIABLE);
            variableDeclarationsParser.parse(token);
        }

        token = synchronize(ROUTINE_START_SET);
    }
}
