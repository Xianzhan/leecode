package com.github.xianzhan.tao.analysis.exception;

import com.github.xianzhan.tao.analysis.symbol.TerminalSymbol;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/10/15
 */
public class SyntacticAnalysisException extends Exception implements Serializable {
    public SyntacticAnalysisException() {};

    public SyntacticAnalysisException(String msg) {
        super(msg);
    }

    public SyntacticAnalysisException(TerminalSymbol token) {
        super(token.toString());
    }
}
