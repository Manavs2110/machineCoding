package org.TicTacToe.model;

public class Board {
    int size;
    Piece[][] cells;    // null means the cell is empty
    int filledCells = 0;

    public Board(int size) {
        this.size = size;
        this.cells = new Piece[size][size];
    }

    public boolean isValidMove(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size && cells[row][col] == null;
    }

    public void placePiece(int row, int col, Piece piece) {
        cells[row][col] = piece;
        filledCells++;
    }

    public boolean isFull() {
        return filledCells == size * size;
    }

    // Only the row, column and diagonals through the last move can have just been completed
    public boolean isWinningMove(int row, int col, Piece piece) {
        boolean rowWin = true, colWin = true, diagonalWin = true, antiDiagonalWin = true;
        for (int i = 0; i < size; i++) {
            if (cells[row][i] != piece) rowWin = false;
            if (cells[i][col] != piece) colWin = false;
            if (cells[i][i] != piece) diagonalWin = false;
            if (cells[i][size - 1 - i] != piece) antiDiagonalWin = false;
        }
        return rowWin || colWin || diagonalWin || antiDiagonalWin;
    }

    public void printBoard() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                System.out.print(cells[row][col] == null ? "- " : cells[row][col] + " ");
            }
            System.out.println();
        }
    }

    public int getSize() {
        return size;
    }
}
