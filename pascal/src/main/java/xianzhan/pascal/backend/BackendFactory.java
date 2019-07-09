package xianzhan.pascal.backend;

import xianzhan.pascal.backend.compiler.CodeGenerator;
import xianzhan.pascal.backend.interpreter.Debugger;
import xianzhan.pascal.backend.interpreter.DebuggerType;
import xianzhan.pascal.backend.interpreter.Executor;
import xianzhan.pascal.backend.interpreter.RuntimeStack;
import xianzhan.pascal.backend.interpreter.debugger.CommandLineDebugger;
import xianzhan.pascal.intermediate.TypeSpec;
import xianzhan.pascal.intermediate.impl.Predefined;

/**
 * A factory class that creates compiler and interpreter components.
 *
 * @author xianzhan
 * @since 2019-05-08
 */
public class BackendFactory {
    /**
     * Create a compiler or an interpreter back end component.
     *
     * @param operation either "compile" or "execute"
     * @param inputPath the input file path
     * @return a compiler or an interpreter back end component.
     * @throws Exception if an error occurred.
     */
    public static Backend createBackend(String operation, String inputPath) throws Exception {
        if ("compile".equalsIgnoreCase(operation)) {
            return new CodeGenerator();
        } else if ("execute".equalsIgnoreCase(operation)) {
            return new Executor(inputPath);
        } else {
            throw new Exception("Backend factory: Invalid operation '" + operation + "'");
        }
    }

    /**
     * Create a debugger.
     *
     * @param type         the type of debugger (COMMAND_LINE or GUI).
     * @param backend      the backend.
     * @param runtimeStack the runtime stack
     * @return the debugger
     */
    public static Debugger createDebugger(DebuggerType type, Backend backend, RuntimeStack runtimeStack) {
        switch (type) {
            case COMMAND_LINE: {
                return new CommandLineDebugger(backend, runtimeStack);
            }

            case GUI: {
                return null;
            }

            default: {
                return null;
            }
        }
    }

    /**
     * Return the default value for a data type.
     *
     * @param type the data type.
     * @return the type descriptor.
     */
    public static Object defaultValue(TypeSpec type) {
        TypeSpec baseType = type.baseType();
        if (baseType == Predefined.integerType) {
            return 0;
        } else if (baseType == Predefined.realType) {
            return 0.0F;
        } else if (baseType == Predefined.booleanType) {
            return Boolean.FALSE;
        } else if (baseType == Predefined.charType) {
            return '#';
        } else {
            // String
            return "#";
        }
    }
}
