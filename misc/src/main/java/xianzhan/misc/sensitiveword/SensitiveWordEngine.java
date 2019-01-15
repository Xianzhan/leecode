package xianzhan.misc.sensitiveword;

import java.util.*;
import java.util.function.BiConsumer;

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

    private SensitiveWordNode rootNode;

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
     * 创建敏感字状态机 !!!
     *
     * @param keywords 敏感字集
     */
    private void createNodeTree(Collection<String> keywords) {
        rootNode = new SensitiveWordNode();

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
        // 防止为调用 createStateMachine() NPE
        rootNode = new SensitiveWordNode();
    }

    /**
     * 替换字符串中的敏感字符为 *
     *
     * @param text 文本
     * @return String 如果参数为 null 或者为空白符, 返回空字符串
     */
    public String replaceSensitiveWord(String text) {
        if (isEmpty(text)) {
            return "";
        }

        char[] chars = text.toCharArray();
        findSensitiveWord(chars, (start, end) -> Arrays.fill(chars, start, end, '*'));
        return new String(chars);
    }

    /**
     * 列举所有字符串所包含的敏感词
     *
     * @param text 字符串
     * @return list(string)
     */
    public List<String> listSensitiveWord(String text) {
        if (isEmpty(text)) {
            return Collections.emptyList();
        }

        List<String> list = new LinkedList<>();
        char[] chars = text.toCharArray();
        findSensitiveWord(chars, (start, end) -> list.add(new String(Arrays.copyOfRange(chars, start, end))));
        return list;
    }

    private void findSensitiveWord(char[] chars, BiConsumer<Integer, Integer> consumer) {
        // 找到敏感词位置

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
                // 起始位
                final int start = position - rollback;
                // 结束位
                final int end = position + 1;

                consumer.accept(start, end);

                rollback = 0;
                tempNode = rootNode;
            } else {
                rollback++;
            }
            position++;
        }
    }

    private boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }
}
