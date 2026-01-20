package org.splitWise.model;

import java.util.Arrays;
import java.util.HashMap;

public enum Type {
    EXPENSE("EXPENSE"),
    SHOW("SHOW"),
    QUIT("QUIT");

    private final String name;
    Type(String s) {
        name = s;
    }
    public String toString(){
        return this.name;
    }
    //get enum from string
    private static final HashMap<String, Type> map = new HashMap<>(values().length,1);

    static {
        Arrays.stream(values()).forEach(p->map.put(p.toString(), p));
    }

    public static Type of(String name){
        return map.get(name);
    }
}
