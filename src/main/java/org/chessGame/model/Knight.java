package org.chessGame.model;

import java.util.List;

public class Knight extends Piece {
    // the 8 L-jumps; one step each, so it jumps over pieces
    private static final int[][] DIRECTIONS = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};

    public Knight(Color color) { super(color); }

    @Override
    public List<Cell> getPossibleMoves(Board board, Cell from) {
        return getMovesInDirections(board, from, DIRECTIONS, false);
    }

    @Override
    public char getSymbol() { return 'N'; }
}
