/**
 * <h1>Interpreter</h1>
 * <pre>
 *             / ICode
 *     Backend
 *        |    \ SymbolTable
 *    Executor
 *        |
 *  StatementExecutor
 *        |
 *        ├ CompoundExecutor
 *        ├ AssignmentExecutor
 *        └ ExpressionExecutor
 * </pre>
 * <p>
 * Class StatementExecutor and each of it's subclasses has an execute() method
 * specialized to execute a particular language construct by interpreting
 * that construct's parse tree. It's return value is always null, except for
 * class ExpressionExecutor, whose return value is the computed value of
 * the expression. Since the intermediate code and the symbol table are
 * both language-independent, StatementExecutor and it's subclasses will
 * also all be language-independent.
 *
 * @see xianzhan.pascal.backend.Backend
 * @see xianzhan.pascal.intermediate.ICode
 * @see xianzhan.pascal.intermediate.SymTab
 * @see xianzhan.pascal.backend.interpreter.Executor
 * @see xianzhan.pascal.backend.interpreter.executor.StatementExecutor
 * @see xianzhan.pascal.backend.interpreter.executor.CompoundExecutor
 * @see xianzhan.pascal.backend.interpreter.executor.AssignmentExecutor
 * @see xianzhan.pascal.backend.interpreter.executor.ExpressionExecutor
 */
package xianzhan.pascal.backend;