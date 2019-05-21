package xianzhan.pascal.backend.compiler;

import xianzhan.pascal.backend.Backend;
import xianzhan.pascal.intermediate.ICode;
import xianzhan.pascal.intermediate.SymTabStack;
import xianzhan.pascal.message.Message;
import xianzhan.pascal.message.MessageType;

/**
 * <h1>CodeGenerator</h1>
 *
 * <p>The code generator for a compiler back end.</p>
 *
 * <p>Copyright (c) 2009 by Ronald Mak</p>
 * <p>For instructional purposes only.  No warranties.</p>
 *
 * @author Ronald Mak
 */
public class CodeGenerator extends Backend {
    /**
     * Process the intermediate code and the symbol table generated by the
     * parser to generate machine-language instructions.
     *
     * @param iCode       the intermediate code.
     * @param symTabStack the symbol table.
     * @throws Exception if an error occurred.
     */
    @Override
    public void process(ICode iCode, SymTabStack symTabStack) throws Exception {
        long startTime = System.currentTimeMillis();
        float elapsedTime = (System.currentTimeMillis() - startTime) / 1000F;
        int instructionCount = 0;

        // Send the compiler summary message.
        Message message = new Message(
                MessageType.COMPILER_SUMMARY,
                new Number[]{instructionCount, elapsedTime}
        );
        sendMessage(message);
    }
}
