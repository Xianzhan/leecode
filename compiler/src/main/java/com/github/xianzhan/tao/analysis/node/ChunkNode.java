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
public abstract class ChunkNode extends AnalysisNode {
    public List<AnalysisNode> lineList = new LinkedList<>();

    @Override
    public void print(int retractNum, PrintStream out) {
        for (AnalysisNode line : lineList) {
            printRetract(retractNum, out);
            line.print(retractNum, out);
            out.println();
        }
    }
}
