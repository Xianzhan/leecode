package com.github.xianzhan.analysis.node;

import com.github.xianzhan.analysis.AnalysisNode;

import java.io.PrintStream;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/10/15
 */
public abstract class WhenNode extends AnalysisNode {
    public ExpressionNode condition;

    @Override
    public void print(int retractNum, PrintStream out) {
        out.print(" when ");
        condition.print(retractNum, out);
    }
}
