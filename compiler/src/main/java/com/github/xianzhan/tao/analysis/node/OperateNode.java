package com.github.xianzhan.tao.analysis.node;

import com.github.xianzhan.tao.analysis.AnalysisNode;

import java.io.PrintStream;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/10/15
 */
public abstract class OperateNode extends AnalysisNode {
    public ExpressionNode expression;
    public WhenNode       condition;

    @Override
    public void print(int retractNum, PrintStream out) {
        expression.print(retractNum, out);
        if (condition != null) {
            condition.print(retractNum, out);
        }
    }
}
