package xianzhan.algorithm.leetcode;

/**
 * No.461 汉明距离
 *
 * @author xianzhan
 * @implNote https://leetcode-cn.com/problems/hamming-distance/description/
 * @since 2018-06-18
 */
public class No461HammingDistance {

    public int hammingDistance(int x, int y) {
        int exclusiveOr = x ^ y;
        return countOne(exclusiveOr);
    }

    private int countOne(int exclusiveOr) {
        int count = 0;
        final int effectiveValue = 31;
        for (int i = 0; i < effectiveValue; i++) {
            if ((exclusiveOr & 1) == 1) {
                count++;
            }
            exclusiveOr >>>= 1;
        }
        return count;
    }

    public static void main(String[] args) {
        No461HammingDistance hammingDistance = new No461HammingDistance();
        System.out.println(hammingDistance.hammingDistance(1, 4));
    }
}
