package com.github.xianzhan.analysis.symbol;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * 描述：
 *
 * @author Lee
 * @since 2017/10/14
 */
public class NonTerminalSymbol {
    public static enum Exp {

        //空白
        SplitSpaceSign, SpaceOrEnter, Space,

        //基本单元
        Enter, This, Null, Boolean,
        Number, Variable, String, RegEx,
        Element,

        //表达式相关
        L0Expression, L0ParamExpression, L0Sign,
        L1Expression, L1ParamExpression,
        L2Expression, L2ParamExpression, L2Sign,
        L3Expression, L3ParamExpression, L3Sign,
        L4Expression, L4ParamExpression, L4Sign,
        L5Expression, L5ParamExpression, L5Sign,
        L6Expression, L6ParamExpression, L6Sign,
        L7Expression, L7ParamExpression, L7Sign,
        L8Expression, L8ParamExpression, L8Sign,
        L9Expression, L9ParamExpression, L9Sign,
        L10Expression, L10ParamExpression, L10Tail, L10TailOperation,
        L11Expression,

        //控制流语法
        Chunk, StartChunk, Line,
        Command, Operate, When,

        DefineVariable, DefineVariableElement,
        DefineFunction, ParamsList,
        IfElseChunk, TryCatch,
        WhileChunk, DoUntilChunk,
        ForEachChunk, ForEachCommand, ForEachCondition,

        //语法糖
        Lambda,
        List, Map, MapEntry,
        Invoker, InvokerBraceless, InvokerBanLambda, InvokerBracelessBanLambda,
        ParamList, ParamListBanTokens,
        Array, Container,
    }

    public final Exp exp;
    public Character sign = null;

    public final ArrayList<Object[]>       expansionList = new ArrayList<>();
    public final ArrayList<TerminalSymbol> banList       = new ArrayList<>();

    public final ArrayList<HashSet<TerminalSymbol>> firstSetList = new
            ArrayList<>();
    public final HashSet<TerminalSymbol>            firstSet     = new HashSet<>();

    public NonTerminalSymbol(Exp exp) {
        this.exp = exp;
    }

    public NonTerminalSymbol ban(TerminalSymbol... args) {
        for (TerminalSymbol node : args) {
            banList.add(node);
        }
        return this;
    }

    public NonTerminalSymbol or(Object... args) {
        expansionList.add(args);
        return this;
    }

    public NonTerminalSymbol sign(char sign) {
        this.sign = sign;
        return this;
    }

    @Override
    public String toString() {
        String str;
        if (exp != null) {
            str = String.valueOf(exp);
        } else {
            LinkedList<String> expansionStr = new LinkedList<>();
            for (Object[] expansion : expansionList) {
                LinkedList<String> signsStr = new LinkedList<>();
                for (Object obj : expansion) {
                    signsStr.add(obj.toString());
                }
                expansionStr.add(String.join(" ", signsStr));
            }
            str = "[" + String.join(" | ", expansionStr) + "]";
        }
        if (sign != null) {
            str += "(" + sign + ")";
        }
        return str;
    }
}
