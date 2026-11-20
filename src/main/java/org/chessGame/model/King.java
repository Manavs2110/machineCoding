package org.chessGame.model;

import java.util.List;

public class King extends Piece {
    private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

    public King(Color color) { super(color); }

    @Override
    public List<Cell> getPossibleMoves(Board board, Cell from) {
        return getMovesInDirections(board, from, DIRECTIONS, false);
    }

    @Override
    public char getSymbol() { return 'K'; }
}
