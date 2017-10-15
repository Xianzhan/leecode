package com.github.xianzhan.intermediate;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/10/15
 */
public class FunctionRecorder {
    private final List<CodeChunk> container = new ArrayList<>();

    int createFunctionBody(CodeChunk body) {
        container.add(body);
        return container.size() - 1;
    }

    CodeChunk getFunctionBody(int id) {
        return container.get(id);
    }

    List<CodeChunk> getContainer() {
        return container;
    }
}
