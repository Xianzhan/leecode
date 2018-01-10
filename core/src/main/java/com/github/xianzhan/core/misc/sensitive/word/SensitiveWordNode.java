package com.github.xianzhan.core.misc.sensitive.word;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lee
 * @since 2018-01-10
 */
class SensitiveWordNode {

    private Map<Character, SensitiveWordNode> subNodes = new HashMap<>();

    /**
     * 用于表示是否为最后一个字符
     */
    private boolean end;

    public void setSubNode(char key, SensitiveWordNode node) {
        subNodes.put(key, node);
    }

    public SensitiveWordNode getSubNode(char key) {
        return subNodes.get(key);
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }
}
