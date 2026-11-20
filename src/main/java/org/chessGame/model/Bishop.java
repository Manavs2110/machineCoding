package org.chessGame.model;

import java.util.List;

public class Bishop extends Piece {
    private static final int[][] DIRECTIONS = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

    public Bishop(Color color) { super(color); }

    @Override
    public List<Cell> getPossibleMoves(Board board, Cell from) {
        return getMovesInDirections(board, from, DIRECTIONS, true);
    }

    @Override
    public char getSymbol() { return 'B'; }
}
