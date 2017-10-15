package com.github.xianzhan.analysis;

import com.github.xianzhan.analysis.exception.SyntacticAnalysisException;
import com.github.xianzhan.analysis.symbol.NonTerminalSymbol.Exp;
import com.github.xianzhan.analysis.symbol.TerminalSymbol;

import java.io.PrintStream;
import java.util.Iterator;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/10/15
 */
public abstract class AnalysisNode {
    private Exp exp = null;

    public abstract void match(AnalysisNode analysisNode) throws SyntacticAnalysisException;

    public abstract void match(TerminalSymbol token) throws SyntacticAnalysisException;

    public abstract void print(int retractNum, PrintStream out);

    public void finish() throws SyntacticAnalysisException {}

    public void printRetract(int retractNum, PrintStream out) {
        for (int i = 0; i < retractNum; ++i) {
            out.print("    ");
        }
    }

    public void printParams(int retractNum, PrintStream out, Iterable<?> container) {
        Iterator<?> it = container.iterator();
        while (it.hasNext()) {
            Object target = it.next();
            if (target instanceof AnalysisNode) {
                ((AnalysisNode) target).print(retractNum, out);
            } else {
                out.print(target);
            }
            if (it.hasNext()) {
                out.print(", ");
            }
        }
    }

    public Exp getExp() {
        return exp;
    }

    protected void setExp(Exp exp) {
        this.exp = exp;
    }
}
