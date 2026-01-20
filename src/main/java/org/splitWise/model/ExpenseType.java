package org.splitWise.model;

import java.util.Arrays;
import java.util.HashMap;

public enum ExpenseType {
    EQUAL("EQUAL"),
    EXACT("EXACT"),
    PERCENT("PERCENT");

    private final String name;
    ExpenseType(String s) {
        name = s;
    }
    public String toString(){
        return this.name;
    }
    //get enum from string
    private static final HashMap<String, ExpenseType> map = new HashMap<>(values().length,1);

    static {
        Arrays.stream(values()).forEach(p->map.put(p.toString(), p));
    }

    public static ExpenseType of(String name){
        return map.get(name);
    }

}
