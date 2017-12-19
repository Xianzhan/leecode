package com.github.xianzhan.lisp;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ASTree {
    private static final int TATOM = 1;
    private static final int TSEXPR = 2;
    private static final int TLMD = 3;
    private static final int TINT = 4;

    ASTree parent;
    private int type;
    ArrayList<ASTree> lst = new ArrayList<>();
    String atom;
    int ival;
    // when lambda
    Closure closure;

    public ASTree(String atom) {
        super();
        if (atom.equals("nil")) {
            setType(TSEXPR);
        } else if (Character.isDigit(atom.charAt(0))) {
            this.ival = Integer.parseInt(atom);
            setType(TINT);
        } else if (atom.startsWith("'") && atom.length() >= 2) {
            // 'a is short for (' a)
            lst.add(new ASTree("quote"));
            lst.add(new ASTree(atom.substring(1)));
            setType(TSEXPR);
        } else {
            this.atom = atom;
            setType(TATOM);
        }
    }

    public ASTree(ASTree parent) {
        super();
        this.parent = parent;
        setType(TSEXPR);
    }

    public ASTree() {
        super();
        this.parent = this;
        setType(TSEXPR);
    }

    public ASTree(ArrayList<ASTree> lst) {
        this.lst = lst;
        this.parent = this;
        setType(TSEXPR);
    }

    public ASTree(Closure closure) {
        // lambda
        this.closure = closure;
        setType(TLMD);
    }

    @Override
    public String toString() {
        if (type == TATOM) return atom;
        if (type == TSEXPR && this.len() == 0) return "nil";
        if (type == TSEXPR) {
            String str = String.join(" ", lst.stream().map(ASTree::toString).collect(Collectors.toList()));
            return "(" + str + ")";
        }
        if (type == TLMD) return closure.toString();
        if (type == TINT) return String.valueOf(ival);
        return "";
    }

    public String toSimpleString() {
        if (type == TATOM) return atom;
        if (type == TSEXPR && this.len() == 0) return "nil";
        if (type == TSEXPR) {
            String str = String.join(" ", lst.stream().map(ASTree::toSimpleString).collect(Collectors.toList()));
            return "(" + str + ")";
        }
        if (type == TLMD) return closure.toSimpleString();
        if (type == TINT) return String.valueOf(ival);
        return "";
    }

    public void begin() {
    }

    public void add(ASTree e) {
        e.parent = this;
        lst.add(e);
    }

    public boolean end() {
        return this == this.parent;
    }

    public int len() {
        return lst.size();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    boolean isList() {
        return type == TSEXPR;
    }

    static ASTree NewBool(boolean b) {
        return b ? new ASTree("#t") : new ASTree();
    }

    ASTree getTail() {
        ArrayList<ASTree> lst = new ArrayList<ASTree>(this.lst);
        lst.remove(0);
        return new ASTree(lst);
    }

    boolean isInt() {
        return type == TINT;
    }

    static ASTree add(ASTree v1, ASTree v2) {
        int i = v1.ival + v2.ival;
        return new ASTree(String.valueOf(i));
    }

    static ASTree mul(ASTree v1, ASTree v2) {
        int i = v1.ival * v2.ival;
        return new ASTree(String.valueOf(i));
    }

    static ASTree sub(ASTree v1, ASTree v2) {
        int i = v1.ival - v2.ival;
        return new ASTree(String.valueOf(i));
    }

    public boolean isDef() {
        if (this.hasHead() && this.getHead().isAtom() && this.getHead().equals("defun")) {
            return true;
        }
        return false;
    }

    boolean isLambda() {
        return type == TLMD;
    }

    boolean isNil() {
        return this.type == TSEXPR && this.len() == 0;
    }

    static ASTree cond(ASTree tree, Env env) throws SyntaxException, RunTimeException {
        while (tree.len() > 0) {
            ASTree entry = tree.getHead();
            ASTree cond = entry.get(0);
            ASTree cond_res = Lisp.eval(cond, env);
            if (cond_res.isTrue()) {
                return Lisp.eval(entry.get(1), env);
            }
            tree = tree.getTail();
        }
        return new ASTree();
    }

    private boolean isTrue() {
        if (type == TATOM)
            return this.equals("#t");
        return this.len() != 0;
    }

    public boolean equals(String str) {
        return this.atom.equals(str);
    }

    public boolean equals(ASTree atom) {
        return this.atom.equals(atom.atom);
    }

    static ASTree cons(ASTree t1, ASTree t2) {
        t2.lst.add(0, t1);
        return t2;
    }

    boolean isAtom() {
        return type == TATOM;
    }

    boolean hasHead() {
        return this.len() > 0;
    }

    ASTree getHead() {
        return this.get(0);
    }

    ASTree get(int i) {
        return this.lst.get(i);
    }

    public boolean isRoot() {
        return this == this.parent;
    }

    public ArrayList<String> toStringList() {
        ArrayList<String> ls = new ArrayList<>();
        for (ASTree t : lst) {
            ls.add(t.atom);
        }
        return ls;
    }
}
