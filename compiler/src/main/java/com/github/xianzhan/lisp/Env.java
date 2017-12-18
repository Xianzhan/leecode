package com.github.xianzhan.lisp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Env {
    private Map<String, ASTree> map = new HashMap<String, ASTree>();
    private Env parent = null;

    public Env(HashMap<String, ASTree> map) {
        this.map = map;
    }
    public Env(String name, ASTree value) {
        map.put(name, value);
    }
    public Env() {
    }
    public Env(ArrayList<String> params, List<ASTree> arg_list) {
        for (int i = 0; i < params.size(); i++) {
            map.put(params.get(0), arg_list.get(i));
        }
    }
    static Env extend(Env old, Env new_) {
        Env e = new Env(new HashMap<String, ASTree>(new_.map));
        e.parent = old;
        return e;
    }
    public ASTree get(String name) {
        if (map.containsKey(name)) return map.get(name);
        if (parent != null) return parent.get(name);
        return null;
    }
    void add(String name, ASTree value) {
        map.put(name, value);
    }
    @Override
    public String toString() {
        List<String> c = map.entrySet().parallelStream().map(e -> e.getKey()+": "+e.getValue().toSimpleString()).collect(Collectors.toList());
        return "{"+String.join(", ", c)+"}";
    }
}
