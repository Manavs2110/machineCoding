package org.TicTacToe;

import org.TicTacToe.model.GameStatus;
import org.TicTacToe.model.Piece;
import org.TicTacToe.model.Player;
import org.TicTacToe.service.TicTacToeGame;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String args[]){
        Scanner scanner = new Scanner(System.in);
        List<Player> players = List.of(new Player("Alice", Piece.X), new Player("Bob", Piece.O));
        TicTacToeGame game = new TicTacToeGame(3, players);

        while (game.getStatus() == GameStatus.IN_PROGRESS) {
            game.getBoard().printBoard();
            Player currentPlayer = game.getCurrentPlayer();
            System.out.print(currentPlayer.getName() + " (" + currentPlayer.getPiece() + "), enter row and col: ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();
            try {
                game.makeMove(row, col);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage() + ", try again");
            }
        }

        game.getBoard().printBoard();
        if (game.getStatus() == GameStatus.WIN) {
            System.out.println(game.getWinner().getName() + " wins!");
        } else {
            System.out.println("It's a draw!");
        }
    }
}
