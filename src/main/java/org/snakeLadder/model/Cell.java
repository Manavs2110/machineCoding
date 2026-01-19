package org.snakeLadder.model;

public class Cell {
    private int i;
    private Obstacle obstacle;

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public Obstacle getObstacle() {
        return obstacle;
    }

    public void setObstacle(Obstacle obstacle) {
        this.obstacle = obstacle;
    }

    public Cell(int i) {
        this.i = i;
    }

    public Cell(int i, Obstacle obstacle) {
        this.i = i;
        this.obstacle = obstacle;
    }
}
