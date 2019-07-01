package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.TokenType;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.impl.DefinitionEnumImpl;

import java.util.EnumSet;

import static xianzhan.pascal.frontend.pascal.PascalTokenType.SEMICOLON;

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
     * @param token    the initial token.
     * @param parentId the symbol table entry of the parent routine's name.
     * @return null
     * @throws Exception if an error occurred.
     */
    public SymTabEntry parse(Token token, SymTabEntry parentId) throws Exception {
        token = synchronize(DECLARATION_START_SET);

        if (token.getType() == PascalTokenType.CONST) {
            // consume CONST
            token = nextToken();

            ConstantDefinitionsParser constantDefinitionsParser = new ConstantDefinitionsParser(this);
            constantDefinitionsParser.parse(token, null);
        }

        token = synchronize(TYPE_START_SET);

        if (token.getType() == PascalTokenType.TYPE) {
            // consume TYPE
            token = nextToken();

            TypeDefinitionsParser typeDefinitionsParser = new TypeDefinitionsParser(this);
            typeDefinitionsParser.parse(token, null);
        }

        token = synchronize(VAR_START_SET);

        if (token.getType() == PascalTokenType.VAR) {
            // consume VAR
            token = nextToken();

            VariableDeclarationsParser variableDeclarationsParser = new VariableDeclarationsParser(this);
            variableDeclarationsParser.setDefinition(DefinitionEnumImpl.VARIABLE);
            variableDeclarationsParser.parse(token, null);
        }

        token = synchronize(ROUTINE_START_SET);
        TokenType tokenType = token.getType();

        while ((tokenType == PascalTokenType.PROCEDURE) || (tokenType == PascalTokenType.FUNCTION)) {
            DeclaredRoutineParser routineParser = new DeclaredRoutineParser(this);
            routineParser.parse(token, parentId);

            // Look for one or more semicolons after a definition.
            token = currentToken();
            if (token.getType() == SEMICOLON) {
                while (token.getType() == SEMICOLON) {
                    // consume the ;
                    token = nextToken();
                }
            }

            token = synchronize(ROUTINE_START_SET);
            tokenType = token.getType();
        }

        return null;
    }
}
