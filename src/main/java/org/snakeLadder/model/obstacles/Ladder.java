package org.snakeLadder.model.obstacles;

import org.snakeLadder.model.Obstacle;
import org.snakeLadder.model.User;

public class Ladder extends Obstacle {
    private int start;
    private int end;

    public Ladder(int start, int end) {
        if(start>end){
            start = start^end;
            end = start^end;
            start=start^end;
        }
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public void performAction(User user){
        user.setPosition(end);
    }
}
