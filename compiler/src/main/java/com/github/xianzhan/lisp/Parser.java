package com.github.xianzhan.lisp;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.Objects;

public class Parser {
    public ArrayList<ASTree> parse() throws IOException {
        return this.parse(System.in);
    }

    public ArrayList<ASTree> parse(InputStream inStream) throws IOException {
        // only support S-expression and atom
        ArrayList<ASTree> ret = new ArrayList<ASTree>();
        ASTree t;
        ASTree cur = null;
        int state = 0;
        StringBuffer sb = new StringBuffer();
        PushbackInputStream in = new PushbackInputStream(inStream);

        while (true) {
            int ci = in.read();
            if (ci == -1) {
                if (sb.length() > 0) {
                    // if only an atom
                    ret.add(new ASTree(sb.toString()));
                }
                return ret;
            }
            char c = (char) ci;
            switch (state) {
                case 0: // initial state
                    if (Character.isWhitespace(c)) {
                        continue;
                    }
                    if (c == '(') {
                        if (cur == null) {
                            cur = new ASTree();
                        } else {
                            t = new ASTree(cur);
                            cur.add(t);
                            t.begin();
                            cur = t;
                        }
                    } else if (c == ')') {
                        if (Objects.requireNonNull(cur, "cur is null").end()) { // expression ends
                            ret.add(cur);
                            cur = null;
                        } else {
                            cur = cur.parent;
                        }
                    } else {
                        // word
                        state = 1;
                        sb.append((char) c);
                    }
                    break;
                case 1: // in an atom
                    if (Character.isWhitespace(c) || c == '(' || c == ')') {
                        ASTree e = new ASTree(sb.toString());
                        Objects.requireNonNull(cur, "cur is null").add(e);

                        sb = new StringBuffer();
                        state = 0;
                        if (c == '(' || c == ')') {
                            in.unread(c);
                        }
                        continue;
                    }
                    sb.append(c);
                    break;
            }
        }

    }
}
