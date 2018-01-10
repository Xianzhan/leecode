package com.github.xianzhan.core.misc.sensitive.word;

import java.util.Collection;

/**
 * 敏感字引擎
 *
 * @author lee
 * @since 2018-01-10
 */
public class SensitiveWordEngine {

    /**
     * 使用 getInstance 方法获取
     *
     * @see #getInstance()
     */
    private static SensitiveWordEngine instance;

    private SensitiveWordNode rootNode = new SensitiveWordNode();

    /**
     * 封装方法, 使实例只有一个方法
     *
     * @param keywords 敏感字集
     * @see #createNodeTree(Collection)
     */
    public static void createStateMachine(Collection<String> keywords) {
        getInstance().createNodeTree(keywords);
    }

    /**
     * 创建敏感字状态机
     *
     * @param keywords 敏感字集
     */
    private void createNodeTree(Collection<String> keywords) {
        for (String keyword : keywords) {
            if (keyword == null) {
                continue;
            }

            char[] chars = keyword.trim().toCharArray();
            int length = chars.length;
            SensitiveWordNode tempNode = rootNode;
            for (int i = 0; i < length; i++) {
                SensitiveWordNode node = tempNode.getSubNode(chars[i]);
                if (node == null) {
                    node = new SensitiveWordNode();
                    tempNode.setSubNode(chars[i], node);
                }
                tempNode = node;
                if (i == length - 1) {
                    tempNode.setEnd(true);
                }
            }
        }
    }

    /**
     * 替换字符串中的敏感字符为 *
     *
     * @param text 文本
     * @return String
     */
    public String replaceSensitiveWord(String text) {
        if (text == null || text.length() == 0) {
            return "";
        }
        return replaceSensitiveWord(text.toCharArray());
    }

    private String replaceSensitiveWord(char[] chars) {

        SensitiveWordNode tempNode = rootNode;
        int rollback = 0;
        int position = 0;

        int length = chars.length;
        while (position < length) {
            tempNode = tempNode.getSubNode(chars[position]);
            if (tempNode == null) {
                position = position - rollback;
                rollback = 0;
                tempNode = rootNode;
            } else if (tempNode.isEnd()) {
                int index = 0;
                int len = rollback + 1;
                for (int i = 0; i <= rollback; i++) {
                    if (index < len) {
                        chars[position - i] = 42; // '*'
                    }
                    index++;
                }

                rollback = 1;
            } else {
                rollback++;
            }
            position++;
        }
        return new String(chars);
    }

    public static SensitiveWordEngine getInstance() {
        if (instance == null) {
            synchronized (SensitiveWordEngine.class) {
                if (instance == null) {
                    instance = new SensitiveWordEngine();
                }
            }
        }
        return instance;
    }

    private SensitiveWordEngine() {

    }
}
