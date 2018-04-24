package xianzhan.neuralNetwork.handwritingRecognition.util;

/**
 * @auther xianzhan
 * @sinese 2018-04-23
 */
public class MathUtils {
    public static double sigmoidValue(Double arg) {
        return (1 / (1 + Math.exp(-arg)));
    }
}
