package com.github.xianzhan.analysis.node;

import com.github.xianzhan.analysis.AnalysisNode;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/10/15
 */
public abstract class LambdaNode extends AnalysisNode {
    public List<String> paramNameList = new LinkedList<>();
    public ChunkNode chunk;

    @Override
    public void print(int retractNum, PrintStream out) {
        out.print(" do(");
        printParams(retractNum, out, paramNameList);
        out.print(")\n");
        if (chunk != null) {
            chunk.print(retractNum + 1, out);
        }
        printRetract(retractNum, out);
        out.print("end");
    }
}
