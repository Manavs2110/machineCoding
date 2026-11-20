package org.TicTacToe.service;

import org.TicTacToe.model.Board;
import org.TicTacToe.model.GameStatus;
import org.TicTacToe.model.Player;

import java.util.List;

public class TicTacToeGame {
    Board board;
    List<Player> players;
    int currentPlayerIndex = 0;
    GameStatus status = GameStatus.IN_PROGRESS;
    Player winner;

    public TicTacToeGame(int boardSize, List<Player> players) {
        this.board = new Board(boardSize);
        this.players = players;
    }

    public GameStatus makeMove(int row, int col) {
        if (status != GameStatus.IN_PROGRESS) throw new RuntimeException("Game is already over");
        if (!board.isValidMove(row, col)) throw new RuntimeException("Invalid move: " + row + "," + col);

        Player currentPlayer = getCurrentPlayer();
        board.placePiece(row, col, currentPlayer.getPiece());

        if (board.isWinningMove(row, col, currentPlayer.getPiece())) {
            status = GameStatus.WIN;
            winner = currentPlayer;
        } else if (board.isFull()) {
            status = GameStatus.DRAW;
        } else {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
        return status;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public GameStatus getStatus() {
        return status;
    }

    public Player getWinner() {
        return winner;
    }

    public Board getBoard() {
        return board;
    }
}
