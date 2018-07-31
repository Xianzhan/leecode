package xianzhan.algorithm.leetcode;

import java.util.LinkedList;
import java.util.List;

/**
 * No.728 自除数
 *
 * @author xianzhan
 * @apiNote https://leetcode-cn.com/problems/self-dividing-numbers/description/
 * @since 2018-06-24
 */
public class No728SelfDividingNumbers {

    public List<Integer> selfDividingNumbers(int left, int right) {
        List<Integer> result = new LinkedList<>();
        for (int i = left; i <= right; i++) {
            if (isSelfDividing(i)) {
                result.add(i);
            }
        }
        return result;
    }

    private boolean isSelfDividing(int i) {
        List<Integer> divList = parserDiv(i);
        for (int div : divList) {
            if (div == 0 || i % div != 0) {
                return false;
            }
        }
        return true;
    }

    private List<Integer> parserDiv(int i) {
        var list = new LinkedList<Integer>();
        while (i != 0) {
            int remainder = i % 10;
            list.add(remainder);
            i /= 10;
        }
        return list;
    }

    public static void main(String[] args) {
        var solution = new No728SelfDividingNumbers();
        System.out.println(solution.selfDividingNumbers(1, 22));
    }
}
