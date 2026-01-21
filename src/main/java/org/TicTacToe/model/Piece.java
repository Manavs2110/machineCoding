package org.TicTacToe.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum Piece {
    X("X"),
    O("O");

    String piece;
    Piece(String piece){
        this.piece = piece;
    }
    @Override
    public String toString(){
        return this.piece;
    }
    private static Map<String, Piece> mp = new HashMap<>(values().length,1);
    static{
        Arrays.stream(values()).forEach(piece1 -> mp.put(piece1.toString(),piece1));
    }
    public static Piece of(String piece){
        return mp.get(piece);
    }
}
