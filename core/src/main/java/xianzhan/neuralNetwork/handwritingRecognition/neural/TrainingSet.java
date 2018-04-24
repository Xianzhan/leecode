package xianzhan.neuralNetwork.handwritingRecognition.neural;

import java.util.ArrayList;

/**
 * @auther xianzhan
 * @sinese 2018-04-23
 */
public class TrainingSet {
    private ArrayList<Integer> inputs;
    private ArrayList<Double>  goodOutput;

    public TrainingSet(ArrayList<Integer> inputs, ArrayList<Double> goodOutput) {
        this.inputs = inputs;
        this.goodOutput = goodOutput;
    }

    public ArrayList<Integer> getInputs() {
        return inputs;
    }

    public ArrayList<Double> getGoodOutput() {
        return goodOutput;
    }
}
