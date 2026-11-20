package org.chessGame;

import org.chessGame.model.Color;
import org.chessGame.model.Player;
import org.chessGame.service.ChessGame;

public class Main {
    public static void main(String args[]) {
        ChessGame game = new ChessGame(new Player("Asha", Color.WHITE), new Player("Ravi", Color.BLACK));

        // Scholar's mate. Row 0 = black's back row, row 7 = white's.
        int[][] moves = {
                {6, 4, 4, 4},   // e2 -> e4
                {1, 4, 3, 4},   // e7 -> e5
                {7, 3, 3, 7},   // Qd1 -> h5
                {1, 0, 4, 0},   // a7 -> a4 : invalid
                {0, 1, 2, 2},   // Nb8 -> c6
                {7, 5, 4, 2},   // Bf1 -> c4
                {0, 6, 2, 5},   // Ng8 -> f6
                {3, 7, 1, 5},   // Qh5 x f7 checkmate
        };
        for (int[] m : moves) {
            try {
                game.makeMove(m[0], m[1], m[2], m[3]);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage() + ", try again");
            }
        }

        game.getBoard().printBoard();
        System.out.println("White king can go to " + game.getValidMoves(7, 4).size() + " cells");
        System.out.println(game.getStatus() + ", winner: " + game.getWinner().getName());
    }
}
