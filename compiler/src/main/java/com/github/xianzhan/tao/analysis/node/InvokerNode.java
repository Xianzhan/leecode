package com.github.xianzhan.tao.analysis.node;

import com.github.xianzhan.tao.analysis.AnalysisNode;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/10/15
 */
public abstract class InvokerNode extends AnalysisNode {
    public List<ExpressionNode> paramList = new LinkedList<>();
    public LambdaNode           lambda    = null;

    @Override
    public void print(int retractNum, PrintStream out) {
        out.print("{");
        printParams(retractNum, out, paramList);
        out.print("}");
        if (lambda != null) {
            lambda.print(retractNum, out);
        }
    }
}
