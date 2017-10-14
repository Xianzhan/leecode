package com.github.xianzhan.analysis.exception;

import java.io.Serializable;

/**
 * 描述：当源代码读完了，如果状态机处于Normal状态，此时应该生成一个EndSymbol。
 * 但如果此时不处于 Normal 状态，那就有问题了，必须抛出一个异常。
 * （这种情况是程序员把源代码本身写错了，例如最后一个字符串少右边的"之类的。)
 *
 * @author Lee
 * @since 2017/10/14
 */
public class LexicalAnalysisException extends Exception implements Serializable {

    public LexicalAnalysisException(char c) {
        super("unexpected '" + c + "'");
    }

    public LexicalAnalysisException(String msg) {
        super("unexpected \"" + msg + "\"");
    }
}
