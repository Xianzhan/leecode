package com.github.xianzhan.vm;

import com.github.xianzhan.intermediate.CodeChunk;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.LinkedList;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/10/16
 */
public class Runtime {
    private final PrintStream out;
    private final InputStream in;

    private final LinkedList<DataChunk> dataStack  = new LinkedList<>();
    private final LinkedList<CodeChunk> codeStack  = new LinkedList<>();
    private final LinkedList<Value>     paramStack = new LinkedList<>();

    private final Interpreter interpreter;

    private boolean catchError = false;

    public Runtime(InputStream in, PrintStream out) {
        this.in = in;
        this.out = out;
        this.interpreter = new Interpreter(this);
    }

    public LinkedList<DataChunk> getDataStack() {
        return dataStack;
    }

    public LinkedList<CodeChunk> getCodeStack() {
        return codeStack;
    }

    public LinkedList<Value> getParamStack() {
        return paramStack;
    }

    public boolean isCatchError() {
        return catchError;
    }

    public void setCatchError(boolean catchError) {
        this.catchError = catchError;
    }

    public Value run(CodeChunk runCodeChunk) {
        dataStack.push(new DataChunk(TaolanNativeObject.Globals));
        codeStack.push(runCodeChunk);

        int nextRunLine = 0;

        while (!codeStack.isEmpty()) {
            DataChunk currentDataChunk = dataStack.getFirst();
            CodeChunk currentCodeChunk = codeStack.getFirst();
            CodeChunk.Code code = currentCodeChunk.getCodeByLine(nextRunLine);
            nextRunLine = interpreter.run(nextRunLine, code, currentDataChunk);
        }
        return paramStack.pop();
    }
}
