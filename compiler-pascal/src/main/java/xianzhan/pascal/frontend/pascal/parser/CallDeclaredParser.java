package xianzhan.pascal.frontend.pascal.parser;

import xianzhan.pascal.frontend.Token;
import xianzhan.pascal.frontend.pascal.PascalParserTD;
import xianzhan.pascal.intermediate.ICodeFactory;
import xianzhan.pascal.intermediate.ICodeNode;
import xianzhan.pascal.intermediate.SymTabEntry;
import xianzhan.pascal.intermediate.impl.ICodeKeyEnumImpl;
import xianzhan.pascal.intermediate.impl.ICodeNodeTypeEnumImpl;

/**
 * Parse a called to a declared procedure or function.
 *
 * @author xianzhan
 * @since 2019-06-30
 */
public class CallDeclaredParser extends CallParser {
    /**
     * Constructor.
     *
     * @param parent the parent parser.
     */
    public CallDeclaredParser(PascalParserTD parent) {
        super(parent);
    }

    /**
     * Parse a call to a declared procedure or function.
     *
     * @param token the initial token.
     * @return the root node of the generated parse tree.
     * @throws Exception if an error occurred.
     */
    @Override
    public ICodeNode parse(Token token) throws Exception {
        // Create the CALL node.
        ICodeNode callNode = ICodeFactory.createICodeNode(ICodeNodeTypeEnumImpl.CALL);
        SymTabEntry pfId = symTabStack.lookup(token.getText().toLowerCase());
        callNode.setAttribute(ICodeKeyEnumImpl.ID, pfId);
        callNode.setTypeSpec(pfId.getTypeSpec());

        // consume procedure or function identifier
        token = nextToken();

        ICodeNode paramsNode = parseActualParameters(token, pfId, true, false, false);

        callNode.addChild(paramsNode);
        return callNode;
    }
}
