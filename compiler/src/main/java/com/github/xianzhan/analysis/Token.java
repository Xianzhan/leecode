package com.github.xianzhan.analysis;

import java.util.HashSet;

/**
 * 描述：用于表示词法分析器 Tokenizer 的产物.
 * 同时, 也作为下一阶段的语法分析器 Parser 的原料
 *
 * @author Lee
 * @since 2017/10/14
 */
public class Token {

    private static final HashSet<String> keywordsSet = new HashSet<>();

    static {
        keywordsSet.add("if");
        keywordsSet.add("when");
        keywordsSet.add("elsif");
        keywordsSet.add("else");
        keywordsSet.add("while");
        keywordsSet.add("begin");
        keywordsSet.add("until");
        keywordsSet.add("for");
        keywordsSet.add("do");
        keywordsSet.add("try");
        keywordsSet.add("catch");
        keywordsSet.add("finally");
        keywordsSet.add("end");
        keywordsSet.add("def");
        keywordsSet.add("var");
        keywordsSet.add("this");
        keywordsSet.add("null");
        keywordsSet.add("throw");
        keywordsSet.add("break");
        keywordsSet.add("continue");
        keywordsSet.add("return");
        keywordsSet.add("operator");
    }

    public static enum Type {
        // 对词法分析器 Tokenizer 隐藏这三种类型的区别
        // 将这三种类型统称 Identifier, 以简化编码
        Keyword,
        Number,
        Identifier,
        //
        Sign,
        Annotation,
        String,
        RegEx,
        Space,
        NewLine,
        EndSymbol;
    }

    final Type   type;
    final String value;

    Token(Type type, String value) {
        switch (type) {
            case Identifier:
                char firstChar = value.charAt(0);
                if (firstChar >= '0' && firstChar < '9') {
                    type = Type.Number;
                } else if (keywordsSet.contains(value)) {
                    type = Type.Keyword;
                }
                break;
            case Annotation:
                value = value.substring(1);
                break;
            case String:
                value = value.substring(1, value.length() - 1);
                break;
            case RegEx:
                value = value.substring(1, value.length() - 1);
                break;
            case EndSymbol:
                value = null;
                break;
            default:
                // do nothing;
        }

        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s(%s) ", type, value);
    }
}
