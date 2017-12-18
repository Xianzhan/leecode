package com.github.xianzhan.lisp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Lisp {

    // http://www.jianshu.com/p/509505d3bd50
    public static ASTree eval(ASTree expr, Env env) throws SyntaxException, RunTimeException {
        // #t
        if (expr.isAtom() && expr.equals("#t")) {
            return expr;
        }

        // look up variable
        if (expr.isAtom()) {
            ASTree v = env.get(expr.atom);
            if (v == null) {
                throw new RuntimeException("undefined variable " + expr.atom);
            }
            return v;
        }

        // only the root of list (7 function)
        if (expr.isList()) {
            if (expr.hasHead()) {
                ASTree head = expr.getHead();
                ASTree tail = expr.getTail();
                return Lisp.evalList(head, tail, env);
            } else {
                return expr;
            }
        }
        return new ASTree();
    }

    static ASTree evalList(ASTree head, ASTree tail, Env env) throws SyntaxException, RunTimeException {
        if (head.isAtom()) {
            // Quote
            if (head.equals("quote") || head.equals("'")) {
                return tail.get(0);
            }
            // Atom
            if (head.equals("atom")) {
                return ASTree.NewBool(Lisp.eval(tail.get(0), env).isAtom());
            }
            // Eq
            if (head.equals("eq")) {
                ASTree v1 = Lisp.eval(tail.get(0), env);
                ASTree v2 = Lisp.eval(tail.get(1), env);
                if (v1.isAtom() && v2.isAtom())
                    return ASTree.NewBool(v1.equals(v2));
                if (v1.isInt() && v2.isInt())
                    return ASTree.NewBool(v1.ival == v2.ival);
                return ASTree.NewBool(v1.isNil() && v2.isNil());
            }
            // Car
            if (head.equals("car")) {
                return Lisp.eval(tail.get(0), env).get(0);
            }
            // Cdr
            if (head.equals("cdr")) {
                return Lisp.eval(tail.get(0), env).getTail();
            }
            // Cons
            if (head.equals("cons")) {
                ASTree v1 = Lisp.eval(tail.get(0), env);
                ASTree v2 = Lisp.eval(tail.get(1), env);
                return ASTree.cons(v1, v2);
            }
            // Cond
            if (head.equals("cond")) {
                return ASTree.cond(tail, env);
            }

            if (head.equals("let")) { // (let (name val) expr)
                ASTree name_val = tail.get(0);
                ASTree val = Lisp.eval(name_val.get(1), env);
                Env env_new = Env.extend(env, new Env(name_val.get(0).atom, val));
                return Lisp.eval(tail.get(1), env_new);
            }

            // defun
            if (head.equals("defun")) { // (defun name value) or (defun name (arg...) e)
                // defun a var or a function
                env = Lisp.doDef(tail, env);
                return tail;
            }

            // lambda
            if (head.equals("lambda")) { // (lambda (arg...) e)
                ASTree _param = tail.get(0);
                ASTree _body = tail.get(1);
                return new ASTree(new Closure(_param.toStringList(), _body, env));
            }

            // + - *
            String[] arith = {"+", "-", "*"};
            if (Arrays.asList(arith).contains(head.atom)) {
                ASTree v1 = Lisp.eval(tail.get(0), env);
                ASTree v2 = Lisp.eval(tail.get(1), env);
                if (head.equals("+")) {
                    return ASTree.add(v1, v2);
                } else if (head.equals("-")) {
                    return ASTree.sub(v1, v2);
                } else if (head.equals("*")) {
                    return ASTree.mul(v1, v2);
                }
            }

            if (head.equals("list")) {
                ArrayList<ASTree> ls = new ArrayList<ASTree>();
                for (ASTree e : tail.lst) {
                    ls.add(Lisp.eval(e, env));
                }
                return new ASTree(ls);
            }
        }
        // apply user defined function
        ASTree hval = Lisp.eval(head, env);
        if (hval.isLambda()) {
            return Lisp.apply(hval, tail.lst, env);
        } else {
            throw new RunTimeException("not a function");
        }
    }

    static ASTree apply(ASTree func, ArrayList<ASTree> lst, Env env) throws SyntaxException, RunTimeException {
//		System.out.printf("apply %s %s\n", func.closure.name, func.toSimpleString());
        Closure c = func.closure;
        Env e = new Env();
        for (int i = 0; i < c.params.size(); i++) {
            String name = c.params.get(i);
            ASTree v = Lisp.eval(lst.get(i), env);
            e.add(name, v);
        }
        Env new_env = Env.extend(c.env, e);
        ASTree r = Lisp.eval(c.body, new_env);
        return r;
    }

    public static Env doDef(ASTree tree, Env env) throws SyntaxException, RunTimeException {
        ASTree name = tree.get(0);
        ASTree value;
        if (tree.len() == 2) {
            // variable
            value = Lisp.eval(tree.get(1), env);
        } else {
            // function
            ASTree _param = tree.get(1);
            ASTree _body = tree.get(2);
            Closure closure = new Closure(_param.toStringList(), _body, env);
            closure.name = name.atom;
            value = new ASTree(closure);
        }
        env.add(name.atom, value);
        return env;
    }

    public static void main(String[] args) {
        Parser p = new Parser();
        try {
            ArrayList<ASTree> tree = p.parse();
            Env env = new Env();
            ASTree r = null;
            for (ASTree t : tree) {
                r = Lisp.eval(t, env);
                if (r.isDef()) {
                    env = Lisp.doDef(r, env);
                }
            }
            System.out.println(r);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SyntaxException e) {
            e.printStackTrace();
        } catch (RunTimeException e) {
            e.printStackTrace();
        }
    }
}
