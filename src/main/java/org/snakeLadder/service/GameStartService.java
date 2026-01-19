package org.snakeLadder.service;

import org.snakeLadder.repository.GameRepository;

public class GameStartService {
    GameRepository gameRepository;
    public GameStartService(GameRepository gameRepository){
        this.gameRepository = gameRepository;
    }

    public void Start(){
        gameRepository.startGame();
    }
}
