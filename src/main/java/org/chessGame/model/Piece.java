package org.chessGame.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    private final Color color;

    public Piece(Color color) { this.color = color; }

    public Color getColor() { return color; }

    // all cells this piece can go to (ignores whether its own king ends up in check; Game checks that)
    public abstract List<Cell> getPossibleMoves(Board board, Cell from);

    public abstract char getSymbol();

    public boolean canMove(Board board, Cell from, Cell to) {
        return getPossibleMoves(board, from).contains(to);
    }

    // slide = true  -> keep going in the direction until blocked (rook, bishop, queen)
    // slide = false -> only one step in each direction (king, knight)
    protected List<Cell> getMovesInDirections(Board board, Cell from, int[][] directions, boolean slide) {
        List<Cell> moves = new ArrayList<>();
        for (int[] d : directions) {
            int r = from.getRow() + d[0];
            int c = from.getCol() + d[1];
            while (board.isInside(r, c)) {
                Cell cell = board.getCell(r, c);
                if (cell.isEmpty()) {
                    moves.add(cell);
                } else {
                    if (cell.getPiece().getColor() != color) moves.add(cell);   // capture enemy
                    break;                                                       // blocked
                }
                if (!slide) break;
                r += d[0];
                c += d[1];
            }
        }
        return moves;
    }
}
