package org.chessGame.model;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    public Pawn(Color color) { super(color); }

    @Override
    public List<Cell> getPossibleMoves(Board board, Cell from) {
        List<Cell> moves = new ArrayList<>();
        int dir = getColor() == Color.WHITE ? -1 : 1;       // white starts at row 6 and moves up
        int startRow = getColor() == Color.WHITE ? 6 : 1;
        int r = from.getRow(), c = from.getCol();

        // one step forward onto an empty cell, two steps from the starting row
        if (board.isInside(r + dir, c) && board.getCell(r + dir, c).isEmpty()) {
            moves.add(board.getCell(r + dir, c));
            if (r == startRow && board.getCell(r + 2 * dir, c).isEmpty()) {
                moves.add(board.getCell(r + 2 * dir, c));
            }
        }

        // diagonal capture
        for (int dc : new int[]{-1, 1}) {
            if (!board.isInside(r + dir, c + dc)) continue;
            Cell cell = board.getCell(r + dir, c + dc);
            if (!cell.isEmpty() && cell.getPiece().getColor() != getColor()) moves.add(cell);
        }
        return moves;
    }

    @Override
    public char getSymbol() { return 'P'; }
}
