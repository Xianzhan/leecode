package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.TypeFactory;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.DefinitionEnumImpl;
import xianzhan.pascal.intermediate.impl.TypeFormEnumImpl;
import xianzhan.pascal.intermediate.impl.TypeKeyEnumImpl;

import java.util.EnumSet;

/**
 * Parse a Pascal record type specification.
 *
 * @author xianzhan
 * @since 2019-06-23
 */
class RecordTypeParser extends TypeSpecificationParser {
    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public RecordTypeParser(PascalParserTD parent) {
        super(parent);
    }

    /**
     * Synchronization set for the END.
     */
    private static final EnumSet<PascalTokenType> END_SET =
            DeclarationsParser.VAR_START_SET.clone();

    static {
        END_SET.add(PascalTokenType.END);
        END_SET.add(PascalTokenType.SEMICOLON);
    }

    /**
     * Parse a Pascal record type specification.
     *
     * @param token the current token.
     * @return the record type specification.
     * @throws Exception if an error occurred.
     */
    @Override
    public TypeSpec parse(Token token) throws Exception {
        TypeSpec recordType = TypeFactory.createType(TypeFormEnumImpl.RECORD);
        // consume RECORD
        token = nextToken();

        // Push a symbol table for the RECORD type specification.
        recordType.setAttribute(TypeKeyEnumImpl.RECORD_SYMTAB, symTabStack.push());

        // Parse the field declarations.
        VariableDeclarationsParser variableDeclarationsParser = new VariableDeclarationsParser(this);
        variableDeclarationsParser.setDefinition(DefinitionEnumImpl.FIELD);
        variableDeclarationsParser.parse(token, null);

        // Pop off the record's symbol table.
        symTabStack.pop();

        // Synchronize at the END.
        token = synchronize(END_SET);

        // Look for the END.
        if (token.getType() == PascalTokenType.END) {
            // consume END
            token = nextToken();
        } else {
            errorHandler.flag(token, PascalErrorCode.MISSING_END, this);
        }

        return recordType;
    }
}
