package org.snakeLadder;

import org.snakeLadder.model.User;
import org.snakeLadder.repository.GameRepository;
import org.snakeLadder.service.GameStartService;
import org.snakeLadder.service.EntityService;

import java.util.Scanner;

public class Main {
    static void main() {
        System.out.println("Enter Board Size");
        Scanner sc = new Scanner(System.in);
        GameRepository gameRepository = new GameRepository(100,6);
        EntityService entityService = new EntityService(gameRepository);
        GameStartService gameStartService = new GameStartService(gameRepository);
        int noOfSnakes = sc.nextInt();
        while(noOfSnakes>0){
            int startPosition = sc.nextInt();
            int endPosition = sc.nextInt();
            entityService.addSnake(startPosition, endPosition);
            noOfSnakes--;
        }
        int noOfLadders = sc.nextInt();
        while(noOfLadders>0){
            int startPosition = sc.nextInt();
            int endPosition = sc.nextInt();
            entityService.addLadder(startPosition, endPosition);
            noOfLadders--;
        }
        int noOfWhales = sc.nextInt();
        while(noOfWhales>0){
            int position = sc.nextInt();
            entityService.addWhale(position);
            noOfWhales--;
        }
        int noOfSharks = sc.nextInt();
        while(noOfSharks>0){
            int position = sc.nextInt();
            entityService.addShark(position);
            noOfSharks--;
        }
        int noOfPlayers = sc.nextInt();
        int i=0;
        while(noOfPlayers>0){
            String player = sc.next();
            entityService.addUser(new User(i++,player));
            noOfPlayers--;
        }
        gameStartService.Start();
    }
}
