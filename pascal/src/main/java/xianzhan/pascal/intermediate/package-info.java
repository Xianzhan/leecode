/**
 * Enter: a new entry into the local symbol table, the table currently at the
 * top of the stack.
 * <p>
 * Look up local: an entry by searching only the local symbol table.
 * <p>
 * Look up: an entry by searching all the symbol tables in the stack.
 *
 * <pre>
 * SymTabStack
 *      |
 *   SymTab
 *      |
 * SymTabEntry
 *      |
 *  SymTabKey
 * </pre>
 *
 * <pre>
 *     ICode
 *       |        ICodeNodeType
 *   ICodeNode <
 *                ICodeKey
 * </pre>
 *
 * @author xianzhan
 * @since 2019-05-23
 */
package xianzhan.pascal.intermediate;