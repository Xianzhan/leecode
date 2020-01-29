/**
 * Language-independent executors in the interpreter back end that will interpret the intermediate code and execute control
 * statements.
 * <p>
 * The executor subclass  StatementExecutor depends on its subclasses, and they each in turn depend on  StatementExecutor .
 * Subclasses  LoopExecutor ,  IfExecutor , and  SelectExecutor also depend on  ExpressionExecutor .
 */
package xianzhan.pascal.backend.interpreter.executor;