package com.github.xianzhan.core.misc.sensitive.word;

import java.util.Arrays;

public class SensitiveWordEngineTest {

    public static void main(String[] args) {
        String[] sensitiveWords = {"12", "he", "5", "你好", "杯"};
        SensitiveWordEngine.createStateMachine(Arrays.asList(sensitiveWords));

        SensitiveWordEngine engine = SensitiveWordEngine.getInstance();
        String text = "0123456789 123456 -+*/abc 你好, hello 世界杯";

        System.out.println(engine.replaceSensitiveWord(text));
    }
}
