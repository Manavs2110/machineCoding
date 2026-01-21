package org.TicTacToe.model;

public class Board {
    String[][] board;
    public Board(int n){
        board = new String[n][n];
        for (String[] strings : board) {
            for (int j = 0; j < board.length; j++) {
                strings[j] = "";
            }
        }
    }
    public void printBoard(){
        for (String[] strings : board) {
            for (int j = 0; j < board.length; j++) {
                System.out.print(strings[j] + " ");
            }
            System.out.println();
        }
    }
    public void setposition(int x, int j,Piece symbol){
        board[x][j] = symbol.toString();
    }
    public String getposition(int x, int j){
        return board[x][j];
    }

    public String[][] getBoard() {
        return board;
    }
}
