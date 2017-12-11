package com.github.xianzhan.tao.analysis.node;

import com.github.xianzhan.tao.analysis.AnalysisNode;
import com.github.xianzhan.tao.analysis.symbol.TerminalSymbol;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/10/15
 */
public class ArrayNode extends AnalysisNode {
    public List<ExpressionNode> content = new ArrayList<>();

    @Override
    public void print(int retractNum, PrintStream out) {
        out.print("[");
        printParams(retractNum, out, content);
        out.print("]");
    }

    @Override
    public void match(AnalysisNode analysisNode) { }

    @Override
    public void match(TerminalSymbol token) { }
}
