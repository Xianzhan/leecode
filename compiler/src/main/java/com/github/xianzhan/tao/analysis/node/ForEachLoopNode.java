package com.github.xianzhan.tao.analysis.node;

import com.github.xianzhan.tao.analysis.AnalysisNode;

import java.io.PrintStream;
import java.util.List;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/10/15
 */
public abstract class ForEachLoopNode extends AnalysisNode {
    public List<AnalysisNode> beforeCommandList = null;
    public List<AnalysisNode> afterCommandList  = null;
    public ExpressionNode loopCondition;
    public ChunkNode      chunk;

    @Override
    public void print(int retractNum, PrintStream out) {
        out.print("for (");
        printParams(retractNum, out, beforeCommandList);
        out.print("; ");
        loopCondition.print(retractNum, out);
        out.print("; ");
        printParams(retractNum, out, afterCommandList);
        out.print(") do\n");
        chunk.print(retractNum + 1, out);
        printRetract(retractNum, out);
        out.print("end");
    }
}
