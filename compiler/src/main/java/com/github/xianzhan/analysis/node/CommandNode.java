package com.github.xianzhan.analysis.node;

import com.github.xianzhan.analysis.AnalysisNode;

import java.io.PrintStream;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/10/15
 */
public abstract class CommandNode extends AnalysisNode {
    public String         command    = null;
    public ExpressionNode expression = null;
    public WhenNode       condition  = null;

    @Override
    public void print(int retractNum, PrintStream out) {
        out.print(command);
        if (expression != null) {
            out.print(" ");
            expression.print(retractNum, out);
        }
        if (condition != null) {
            condition.print(retractNum, out);
        }
    }
}
