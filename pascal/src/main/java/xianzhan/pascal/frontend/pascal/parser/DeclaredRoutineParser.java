package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.TokenType;
import xianzhan.pascal.frontend.pascal.PascalErrorCode;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.frontend.pascal.PascalTokenType;
import xianzhan.pascal.intermediate.Definition;
import xianzhan.pascal.intermediate.ICode;
import xianzhan.pascal.intermediate.ICodeFactory;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.SymTab;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.TypeForm;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.DefinitionEnumImpl;
import xianzhan.pascal.intermediate.impl.Predefined;
import xianzhan.pascal.intermediate.impl.RoutineCodeEnumImpl;
import xianzhan.pascal.intermediate.impl.SymTabKeyImpl;
import xianzhan.pascal.intermediate.impl.TypeFormEnumImpl;

import java.util.ArrayList;
import java.util.EnumSet;

/**
 * Parse a main program routine or a declared procedure or function.
 *
 * @author xianzhan
 * @since 2019-06-30
 */
public class DeclaredRoutineParser extends DeclarationsParser {
    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public DeclaredRoutineParser(PascalParserTD parent) {
        super(parent);
    }

    /**
     * counter for dummy routine names
     */
    private static int dummyCounter = 0;

    /**
     * Parse a standard subroutine declaration.
     *
     * @param token    the initial token.
     * @param parentId the symbol table entry of the parent routine's name.
     * @return the symbol table entry of the declared routine's name.
     * @throws Exception if an error occurred.
     */
    @Override
    public SymTabEntry parse(Token token, SymTabEntry parentId) throws Exception {
        Definition routineDefinition;
        String dummyName;
        SymTabEntry routineId;
        TokenType routineType = token.getType();

        // Initialize.
        switch ((PascalTokenType) routineType) {

            case PROGRAM: {
                // consume PROGRAM
                token = nextToken();
                routineDefinition = DefinitionEnumImpl.PROGRAM;
                dummyName = "DummyProgramName".toLowerCase();
                break;
            }

            case PROCEDURE: {
                // consume PROCEDURE
                token = nextToken();
                routineDefinition = DefinitionEnumImpl.PROCEDURE;
                dummyName = "DummyProcedureName_".toLowerCase() +
                        String.format("%03d", ++dummyCounter);
                break;
            }

            case FUNCTION: {
                // consume FUNCTION
                token = nextToken();
                routineDefinition = DefinitionEnumImpl.FUNCTION;
                dummyName = "DummyFunctionName_".toLowerCase() +
                        String.format("%03d", ++dummyCounter);
                break;
            }

            default: {
                routineDefinition = DefinitionEnumImpl.PROGRAM;
                dummyName = "DummyProgramName".toLowerCase();
                break;
            }
        }

        // Parse the routine name.
        routineId = parseRoutineName(token, dummyName);
        routineId.setDefinition(routineDefinition);

        token = currentToken();

        // Create new intermediate code for the routine.
        ICode iCode = ICodeFactory.createICode();
        routineId.setAttribute(SymTabKeyImpl.ROUTINE_ICODE, iCode);
        routineId.setAttribute(SymTabKeyImpl.ROUTINE_ROUTINES, new ArrayList<SymTabEntry>());

        // Push the routine's new symbol table onto the stack.
        // If it was forwarded, push its existing symbol table.
        if (routineId.getAttribute(SymTabKeyImpl.ROUTINE_CODE) == RoutineCodeEnumImpl.FORWARD) {
            SymTab symTab = (SymTab) routineId.getAttribute(SymTabKeyImpl.ROUTINE_SYMTAB);
            symTabStack.push(symTab);
        } else {
            routineId.setAttribute(SymTabKeyImpl.ROUTINE_SYMTAB, symTabStack.push());
        }

        // Program: Set the program identifier in the symbol table stack.
        if (routineDefinition == DefinitionEnumImpl.PROGRAM) {
            symTabStack.setProgramId(routineId);
        }

        // Non-forwarded procedure or function: Append to the parent's list
        //                                      of routines.
        else if (routineId.getAttribute(SymTabKeyImpl.ROUTINE_CODE) != RoutineCodeEnumImpl.FORWARD) {
            ArrayList<SymTabEntry> subroutines = (ArrayList<SymTabEntry>) parentId.getAttribute(SymTabKeyImpl.ROUTINE_ROUTINES);
            subroutines.add(routineId);
        }

        // If the routine was forwarded, there should not be
        // any formal parameters or a function return type.
        // But parse them anyway if they're there.
        if (routineId.getAttribute(SymTabKeyImpl.ROUTINE_CODE) == RoutineCodeEnumImpl.FORWARD) {
            if (token.getType() != PascalTokenType.SEMICOLON) {
                errorHandler.flag(token, PascalErrorCode.ALREADY_FORWARDED, this);
                parseHeader(token, routineId);
            }
        }

        // Parse the routine's formal parameters and function return type.
        else {
            parseHeader(token, routineId);
        }

        // Look for the semicolon.
        token = currentToken();
        if (token.getType() == PascalTokenType.SEMICOLON) {
            do {
                // consume ;
                token = nextToken();
            } while (token.getType() == PascalTokenType.SEMICOLON);
        } else {
            errorHandler.flag(token, PascalErrorCode.MISSING_SEMICOLON, this);
        }

        // Parse the routine's block or forward declaration.
        if ((token.getType() == PascalTokenType.IDENTIFIER) && (token.getText().equalsIgnoreCase("forward"))) {
            // consume forward
            token = nextToken();
            routineId.setAttribute(SymTabKeyImpl.ROUTINE_CODE, RoutineCodeEnumImpl.FORWARD);
        } else {
            routineId.setAttribute(SymTabKeyImpl.ROUTINE_CODE, RoutineCodeEnumImpl.DECLARED);

            BlockParser blockParser = new BlockParser(this);
            ICodeNode rootNode = blockParser.parse(token, routineId);
            iCode.setRoot(rootNode);
        }

        // Pop the routine's symbol table off the stack.
        symTabStack.pop();

        return routineId;
    }

    /**
     * Parse a routine's name.
     *
     * @param token     the current token.
     * @param dummyName a dummy name in case of parsing problem.
     * @return the symbol table entry of the declared routine's name.
     * @throws Exception if an error occurred.
     */
    private SymTabEntry parseRoutineName(Token token, String dummyName) throws Exception {
        SymTabEntry routineId = null;

        // Parse the routine name identifier.
        if (token.getType() == PascalTokenType.IDENTIFIER) {
            String routineName = token.getText().toLowerCase();
            routineId = symTabStack.lookupLocal(routineName);

            // Not already defined locally: Enter into the local symbol table.
            if (routineId == null) {
                routineId = symTabStack.enterLocal(routineName);
            }

            // If already defined, it should be a forward definition.
            else if (routineId.getAttribute(SymTabKeyImpl.ROUTINE_CODE) != RoutineCodeEnumImpl.FORWARD) {
                routineId = null;
                errorHandler.flag(token, PascalErrorCode.IDENTIFIER_REDEFINED, this);
            }

            // consume routine name identifier
            token = nextToken();
        } else {
            errorHandler.flag(token, PascalErrorCode.MISSING_IDENTIFIER, this);
        }

        // If necessary, create a dummy routine name symbol table entry.
        if (routineId == null) {
            routineId = symTabStack.enterLocal(dummyName);
        }

        return routineId;
    }

    /**
     * Parse a routine's formal parameter list and the function return type.
     *
     * @param token     the current token.
     * @param routineId the symbol table entry of the declared routine's name.
     * @throws Exception if an error occurred.
     */
    private void parseHeader(Token token, SymTabEntry routineId) throws Exception {
        // Parse the routine's formal parameters.
        parseFormalParameters(token, routineId);
        token = currentToken();

        // If this is a function, parse and set its return type.
        if (routineId.getDefinition() == DefinitionEnumImpl.FUNCTION) {
            VariableDeclarationsParser variableDeclarationsParser =
                    new VariableDeclarationsParser(this);
            variableDeclarationsParser.setDefinition(DefinitionEnumImpl.FUNCTION);
            TypeSpec type = variableDeclarationsParser.parseTypeSpec(token);

            token = currentToken();

            // The return type cannot be an array or record.
            if (type != null) {
                TypeForm form = type.getForm();
                if ((form == TypeFormEnumImpl.ARRAY) || (form == TypeFormEnumImpl.RECORD)) {
                    errorHandler.flag(token, PascalErrorCode.INVALID_TYPE, this);
                }
            }

            // Missing return type.
            else {
                type = Predefined.undefinedType;
            }

            routineId.setTypeSpec(type);
            token = currentToken();
        }
    }

    /**
     * Synchronization set for a formal parameter sublist.
     */
    private static final EnumSet<PascalTokenType> PARAMETER_SET =
            DeclarationsParser.DECLARATION_START_SET.clone();

    static {
        PARAMETER_SET.add(PascalTokenType.VAR);
        PARAMETER_SET.add(PascalTokenType.IDENTIFIER);
        PARAMETER_SET.add(PascalTokenType.RIGHT_PAREN);
    }

    /**
     * Synchronization set for a formal parameter sublist.
     */
    private static final EnumSet<PascalTokenType> LEFT_PAREN_SET =
            DeclarationsParser.DECLARATION_START_SET.clone();

    static {
        LEFT_PAREN_SET.add(PascalTokenType.LEFT_PAREN);
        LEFT_PAREN_SET.add(PascalTokenType.SEMICOLON);
        LEFT_PAREN_SET.add(PascalTokenType.COLON);
    }

    /**
     * Synchronization set for the closing right parenthesis.
     */
    private static final EnumSet<PascalTokenType> RIGHT_PAREN_SET =
            LEFT_PAREN_SET.clone();

    static {
        RIGHT_PAREN_SET.remove(PascalTokenType.LEFT_PAREN);
        RIGHT_PAREN_SET.add(PascalTokenType.RIGHT_PAREN);
    }

    /**
     * Parse a routine's formal parameter list.
     *
     * @param token     the current token.
     * @param routineId the symbol table entry of the declared routine's name.
     * @throws Exception if an error occurred.
     */
    protected void parseFormalParameters(Token token, SymTabEntry routineId) throws Exception {
        // Parse the formal parameters if there is an opening left parenthesis.
        token = synchronize(LEFT_PAREN_SET);
        if (token.getType() == PascalTokenType.LEFT_PAREN) {
            // consume (
            token = nextToken();

            ArrayList<SymTabEntry> params = new ArrayList<>();

            token = synchronize(PARAMETER_SET);
            TokenType tokenType = token.getType();

            // Loop to parse sublists of formal parameter declarations.
            while ((tokenType == PascalTokenType.IDENTIFIER) || (tokenType == PascalTokenType.VAR)) {
                params.addAll(parseParamSublist(token, routineId));
                token = currentToken();
                tokenType = token.getType();
            }

            // Closing right parenthesis.
            if (token.getType() == PascalTokenType.RIGHT_PAREN) {
                // consume )
                token = nextToken();
            } else {
                errorHandler.flag(token, PascalErrorCode.MISSING_RIGHT_PAREN, this);
            }

            routineId.setAttribute(SymTabKeyImpl.ROUTINE_PARMS, params);
        }
    }

    /**
     * Synchronization set to follow a formal parameter identifier.
     */
    private static final EnumSet<PascalTokenType> PARAMETER_FOLLOW_SET =
            EnumSet.of(
                    PascalTokenType.COLON,
                    PascalTokenType.RIGHT_PAREN,
                    PascalTokenType.SEMICOLON
            );

    static {
        PARAMETER_FOLLOW_SET.addAll(DeclarationsParser.DECLARATION_START_SET);
    }

    /**
     * Synchronization set for the , token.
     */
    private static final EnumSet<PascalTokenType> COMMA_SET =
            EnumSet.of(
                    PascalTokenType.COMMA,
                    PascalTokenType.COLON,
                    PascalTokenType.IDENTIFIER,
                    PascalTokenType.RIGHT_PAREN,
                    PascalTokenType.SEMICOLON
            );

    static {
        COMMA_SET.addAll(DeclarationsParser.DECLARATION_START_SET);
    }

    /**
     * Parse a sublist of formal parameter declarations.
     *
     * @param token     the current token.
     * @param routineId the symbol table entry of the declared routine's name.
     * @return the sublist of symbol table entries for the parm identifiers.
     * @throws Exception if an error occurred.
     */
    private ArrayList<SymTabEntry> parseParamSublist(Token token, SymTabEntry routineId) throws Exception {
        boolean isProgram = routineId.getDefinition() == DefinitionEnumImpl.PROGRAM;
        Definition paramDefinition = isProgram ? DefinitionEnumImpl.PROGRAM_PARM : null;
        TokenType tokenType = token.getType();

        // VAR or value parameter?
        if (tokenType == PascalTokenType.VAR) {
            if (!isProgram) {
                paramDefinition = DefinitionEnumImpl.VAR_PARM;
            } else {
                errorHandler.flag(token, PascalErrorCode.INVALID_VAR_PARM, this);
            }

            // consume VAR
            token = nextToken();
        } else if (!isProgram) {
            paramDefinition = DefinitionEnumImpl.VALUE_PARM;
        }

        // Parse the parameter sublist and its type specification.
        VariableDeclarationsParser variableDeclarationsParser =
                new VariableDeclarationsParser(this);
        variableDeclarationsParser.setDefinition(paramDefinition);
        ArrayList<SymTabEntry> sublist = variableDeclarationsParser.parseIdentifierSublist(token, PARAMETER_FOLLOW_SET, COMMA_SET);
        token = currentToken();
        tokenType = token.getType();

        if (!isProgram) {

            // Look for one or more semicolons after a sublist.
            if (tokenType == PascalTokenType.SEMICOLON) {
                while (token.getType() == PascalTokenType.SEMICOLON) {
                    // consume the ;
                    token = nextToken();
                }
            }

            // If at the start of the next sublist, then missing a semicolon.
            else if (VariableDeclarationsParser.NEXT_START_SET.contains(tokenType)) {
                errorHandler.flag(token, PascalErrorCode.MISSING_SEMICOLON, this);
            }

            token = synchronize(PARAMETER_SET);
        }

        return sublist;
    }
}
