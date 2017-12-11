package com.github.xianzhan.tao.intermediate;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/10/15
 */
public class IntermediateCodeExpression extends Exception implements
                                                          Serializable {
    public IntermediateCodeExpression() {}

    ;

    public IntermediateCodeExpression(String msg) {
        super(msg);
    }

    ;
}
