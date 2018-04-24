package xianzhan.neuralNetwork.handwritingRecognition.data;

import xianzhan.neuralNetwork.handwritingRecognition.neural.TrainingSet;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

/**
 * @auther xianzhan
 * @sinese 2018-04-23
 */
public class ReadWriteFile {
    public static ArrayList<TrainingSet> readTrainingSets() {
        ArrayList<TrainingSet> trainingSets = new ArrayList<>();

        for (int i = 0; i < 26; i++) {
            char letterValue = (char) (i + 65);
            String letter = String.valueOf(letterValue);
            for (ArrayList<Integer> list : readFromFile("neuralNetwork/handwritingRecognition/" + letter + ".txt")) {
                trainingSets.add(new TrainingSet(list, GoodOutputs.getInstance().getGoodOutput(letter)));
            }
        }

        return trainingSets;
    }

    // todo
    private static ArrayList<ArrayList<Integer>> readFromFile(String filename) {
        ArrayList<ArrayList<Integer>> inputs = new ArrayList<>();

        try {
            Class<ReadWriteFile> clazz = ReadWriteFile.class;
            ClassLoader classLoader = clazz.getClassLoader();
            URL resource = classLoader.getResource(filename);
            System.out.println(resource);

            if (resource != null) {

                InputStream in = resource.openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    ArrayList<Integer> input = new ArrayList<>();
                    for (int i = 0; i < line.length(); i++) {
                        int value = 0;
                        try {
                            value = Integer.parseInt(String.valueOf(line.charAt(i)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        input.add(value);
                    }
                    inputs.add(input);
                }
                reader.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputs;
    }

    public static void saveToFile(ArrayList<Integer> input, String filename) {
        try {
            File file = new File("resources/" + filename + ".txt");
            PrintWriter pw = new PrintWriter(new FileOutputStream(file, true));
            for (Integer i : input) {
                pw.write(Integer.toString(i));
            }
            pw.write("\n");
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
