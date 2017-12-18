package com.github.xianzhan.lisp;

import java.util.ArrayList;

public class Closure {
    Env env;
    ArrayList<String> params;
    ASTree body;
    public String name; // help debug

    public Closure(ArrayList<String> params, ASTree body, Env env) {
        super();
        this.params = params;
        this.body = body;
        this.env = env;
    }

    @Override
    public String toString() {
        return "Closure [env=" + env + ", param=" + params + ", body=" + body + "]";
    }

    public String toSimpleString() {
        return "Closure[" + params + "...]";
    }
}
