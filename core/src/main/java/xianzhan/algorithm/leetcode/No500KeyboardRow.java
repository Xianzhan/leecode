package xianzhan.algorithm.leetcode;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * No.500 键盘行
 *
 * @author xianzhan
 * @implNote https://leetcode-cn.com/problems/keyboard-row/description/
 * @since 2018-06-24
 */
public class No500KeyboardRow {
    private List<Character> q;
    private List<Character> a;
    private List<Character> z;

    {
        q = Arrays.asList('q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p');
        a = Arrays.asList('a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l');
        z = Arrays.asList('z', 'x', 'c', 'v', 'b', 'n', 'm');
    }

    public String[] findWords(String[] words) {
        List<String> list = new LinkedList<>();
        for (String word : words) {
            if (inTheSameLine(word)) {
                list.add(word);
            }
        }
        return list.toArray(new String[0]);
    }

    private boolean inTheSameLine(String word) {
        char[] chars = word.toLowerCase().toCharArray();
        int length = chars.length;
        if (length == 1) {
            return true;
        }
        List<Character> check = choose(chars[0]);
        for (int i = 1; i < length; i++) {
            if (!check.contains(chars[i])) {
                return false;
            }
        }
        return true;
    }

    private List<Character> choose(char aChar) {
        if (a.contains(aChar)) {
            return a;
        } else if (q.contains(aChar)) {
            return q;
        } else {
            return z;
        }
    }

    public static void main(String[] args) {
        var solution = new No500KeyboardRow();
        String[] arr = {"Hello", "Alaska", "Dad", "Peace"};
        System.out.println(Arrays.toString(solution.findWords(arr)));
    }
}
