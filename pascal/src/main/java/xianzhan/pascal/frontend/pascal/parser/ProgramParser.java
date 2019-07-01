package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.SymTabEntry;

import java.util.EnumSet;

/**
 * Parse a Pascal program.
 *
 * @author xianzhan
 * @since 2019-06-30
 */
public class ProgramParser extends DeclarationsParser {
    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public ProgramParser(PascalParserTD parent) {
        super(parent);
    }

    /**
     * Synchronization set to start a program.
     */
    static final EnumSet<PascalTokenType> PROGRAM_START_SET =
            EnumSet.of(
                    PascalTokenType.PROGRAM,
                    PascalTokenType.SEMICOLON
            );

    static {
        PROGRAM_START_SET.addAll(DeclarationsParser.DECLARATION_START_SET);
    }

    /**
     * Parse a program.
     *
     * @param token    the initial token.
     * @param parentId the symbol table entry of the parent routine's name.
     * @return null
     * @throws Exception if an error occurred.
     */
    @Override
    public SymTabEntry parse(Token token, SymTabEntry parentId) throws Exception {
        token = synchronize(PROGRAM_START_SET);

        // Parse the program.
        DeclaredRoutineParser routineParser = new DeclaredRoutineParser(this);
        routineParser.parse(token, parentId);

        // Look for the final period.
        token = currentToken();
        if (token.getType() != PascalTokenType.DOT) {
            errorHandler.flag(token, PascalErrorCode.MISSING_PERIOD, this);
        }

        return null;
    }
}
