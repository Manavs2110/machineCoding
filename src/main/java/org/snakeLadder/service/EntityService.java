package org.snakeLadder.service;


import org.snakeLadder.model.User;
import org.snakeLadder.model.obstacles.Ladder;
import org.snakeLadder.model.obstacles.Shark;
import org.snakeLadder.model.obstacles.Snake;
import org.snakeLadder.model.obstacles.Whale;
import org.snakeLadder.repository.GameRepository;

public class EntityService {
    GameRepository gameRepository;
    public EntityService(GameRepository gameRepository){
        this.gameRepository = gameRepository;
    }
    public void addUser(User user){
        gameRepository.addUser(user);
    }

    public void addSnake(int start,int end){
        Snake snake = new Snake(start, end);
        gameRepository.addObstacle(snake,start);
    }

    public void addLadder(int start,int end){
        Ladder ladder = new Ladder(start, end);
        gameRepository.addObstacle(ladder,start);
    }
    public void addWhale(int position){
        Whale whale = new Whale(position);
        gameRepository.addObstacle(whale, position);
    }
    public void addShark(int position){
        Shark whale = new Shark(position);
        gameRepository.addObstacle(whale, position);
    }

}
