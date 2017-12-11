package com.github.xianzhan.tao.analysis.node;

import com.github.xianzhan.tao.analysis.AnalysisNode;

import java.io.PrintStream;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/10/15
 */
public abstract class DefineVariableNode extends AnalysisNode {
    public String variableName;
    public ExpressionNode initValue = null;

    @Override
    public void print(int retractNum, PrintStream out) {
        out.print("var ");
        out.print(variableName);
        if (initValue != null) {
            out.print(" = ");
            initValue.print(retractNum, out);
        }
    }
}
