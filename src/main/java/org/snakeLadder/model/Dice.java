package org.snakeLadder.model;

import java.util.Random;

public class Dice {
    private int faces;
    public Dice(int faces){
        this.faces = faces;
    }
    public int roll(){
        Random random = new Random();
        return (1+random.nextInt(faces));
    }
}
