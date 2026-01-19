package org.snakeLadder.repository;

import org.snakeLadder.model.Cell;
import org.snakeLadder.model.Dice;
import org.snakeLadder.model.Obstacle;
import org.snakeLadder.model.User;

import java.util.*;

public class GameRepository {
    Queue<User> users;
    List<Cell> board;
    Dice dice;
    public GameRepository(int n, int diceFaces){
        users = new LinkedList<>();
        dice = new Dice(diceFaces);
        board = new LinkedList<>();
        for(int i=0;i<n;i++){
            board.add(new Cell(i));
        }
    }

    public void addUser(User user){
        users.add(user);
    }


    public void addObstacle(Obstacle obstacle , int position) {
        board.get(position).setObstacle(obstacle);
    }


    public void startGame() {
        String Winner = "";
        while(true){
            int roll = dice.roll();
            User currentUser = users.poll();
            users.add(currentUser);
            if(!currentUser.canMove()) continue;
            String currentUserName = currentUser.getUserName();
            int currentPosition = currentUser.getPosition();
            int endPosition = currentPosition+roll;
            if(endPosition > board.size()) continue;

            Obstacle obstacle = board.get(endPosition).getObstacle();
            if( obstacle!= null )obstacle.performAction(currentUser);
            else currentUser.setPosition(endPosition);
            System.out.println(currentUserName+" rolled a "+roll+" and moved from "+currentPosition+" to "+currentUser.getPosition());
            if(currentUser.getPosition() == board.size()){
                Winner = currentUserName;
                break;
            }

        }
        System.out.println(Winner+" wins the game");
    }

}
