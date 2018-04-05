package xianzhan.core.misc.sensitive_word;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

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
        rootNode = new SensitiveWordNode(); // 防止为调用 createStateMachine() NPE
    }

    /**
     * 替换字符串中的敏感字符为 *
     *
     * @param text 文本
     * @return String
     */
    public String replaceSensitiveWord(String text) {
        if (isEmpty(text)) {
            return "";
        }

        char[] chars = text.toCharArray();
        findSensitiveWord(chars, idx -> Arrays.fill(chars, idx[0], idx[1], '*'));
        return new String(chars);
    }

    /**
     * 列举所有字符串所包含的敏感词
     *
     * @param text 字符串
     * @return list(string)
     */
    public List<String> listSensitiveWord(String text) {
        List<String> list = new LinkedList<>();
        if (isEmpty(text)) {
            return list;
        }

        char[] chars = text.toCharArray();
        findSensitiveWord(chars, idx -> list.add(new String(Arrays.copyOfRange(chars, idx[0], idx[1]))));
        return list;
    }

    // core !!!
    private void findSensitiveWord(char[] chars, Consumer<int[]> consumer) {
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
                int[] idx = new int[2];
                idx[0] = position - rollback; // 起始位
                idx[1] = position + 1; // 结束位

                consumer.accept(idx);

                rollback = 0;
                tempNode = rootNode;
            } else {
                rollback++;
            }
            position++;
        }
    }

    private boolean isEmpty(String text) {
        return text == null || text.trim().length() == 0;
    }
}
