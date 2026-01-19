package org.snakeLadder.model;

public class User {
    private int userId;
    private String userName;
    private int position;
    private boolean skipNextMove;

    public User(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        this.position=0;
        this.skipNextMove=false;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isSkipNextMove() {
        return skipNextMove;
    }

    public void setSkipNextMove(boolean skipNextMove) {
        this.skipNextMove = skipNextMove;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean canMove(){
        if(this.skipNextMove) {
            this.skipNextMove = false;
            return false;
        }
        return true;
    }
}
