package org.snakeLadder.model.obstacles;

import org.snakeLadder.model.Obstacle;
import org.snakeLadder.model.User;

public class Whale extends Obstacle {
    private int position;

    public Whale(int position) {
        this.position = position;
    }

    @Override
    public void performAction(User user){
        user.setPosition(position-2);
    }
}
