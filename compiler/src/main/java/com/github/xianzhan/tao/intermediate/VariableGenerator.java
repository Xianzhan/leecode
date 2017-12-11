package com.github.xianzhan.tao.intermediate;

import com.github.xianzhan.tao.analysis.node.ElementNode;
import com.github.xianzhan.tao.intermediate.CodeChunk.Command;
import com.github.xianzhan.tao.intermediate.CodeChunk.ImmediateNumber;
import com.github.xianzhan.tao.intermediate.CodeChunk.ImmediateType;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/10/15
 */
public class VariableGenerator {
    final static VariableGenerator instance = new VariableGenerator();

    int defVariable(String varName, ChunkContext context) throws IntermediateCodeExpression {
        if (context.variableRecorder.contains(varName)) {
            throw new IntermediateCodeExpression("variable has defined " + varName);
        }
        int varIndex = context.variablePool.createIndex();
        context.variableRecorder.define(varName, varIndex);
        return varIndex;
    }

    void getAttributesValue(int var, ElementNode element, ChunkContext context) throws IntermediateCodeExpression {
        if (element.fromThis) {
            context.codeChunk.push(Command.Rel, var, getStringImmediateNumber(element.value));
        } else if (element.fromConstructor) {
            context.codeChunk.push(Command.RelStatic, var, getStringImmediateNumber(element.value));
        }
    }

    void setAttributeVariableValue(int resultVar, int setVar, ElementNode element, ChunkContext context) throws IntermediateCodeExpression {
        if (element.fromThis) {
            context.codeChunk.push(Command.Move, resultVar, setVar, -1, getStringImmediateNumber(element.value));
        } else if (element.fromConstructor) {
            context.codeChunk.push(Command.MoveStatic, resultVar, setVar, -1, getStringImmediateNumber(element.value));
        }
    }

    private ImmediateNumber getStringImmediateNumber(String variableName) {
        ImmediateNumber in = new ImmediateNumber();
        in.type = ImmediateType.String;
        in.stringValue = variableName;
        return in;
    }
}
