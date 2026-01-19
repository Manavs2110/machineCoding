package org.snakeLadder.model.obstacles;

import org.snakeLadder.model.Obstacle;
import org.snakeLadder.model.User;

public class Shark extends Obstacle {
    private int position;

    public Shark(int position) {
        this.position = position;
    }
    @Override
    public void performAction(User user){
        user.setSkipNextMove(true);
    }
}
