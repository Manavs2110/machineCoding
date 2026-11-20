package org.chessGame.model;

import java.util.List;

public class Rook extends Piece {
    private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    public Rook(Color color) { super(color); }

    @Override
    public List<Cell> getPossibleMoves(Board board, Cell from) {
        return getMovesInDirections(board, from, DIRECTIONS, true);
    }

    @Override
    public char getSymbol() { return 'R'; }
}
