package com.github.xianzhan.analysis.node;

import com.github.xianzhan.analysis.AnalysisNode;

import java.io.PrintStream;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/10/15
 */
public abstract class StartChunkNode extends AnalysisNode {
    public ChunkNode chunk;

    @Override
    public void print(int retractNum, PrintStream out) {
        chunk.print(retractNum, out);
    }
}
