package org.chessGame.service;

import org.chessGame.model.*;

import java.util.ArrayList;
import java.util.List;

public class ChessGame {
    private final Board board = new Board();
    private final Player[] players;
    private int turn = 0;
    private GameStatus status = GameStatus.ACTIVE;

    public ChessGame(Player white, Player black) {
        this.players = new Player[]{white, black};
    }

    public void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (status != GameStatus.ACTIVE) throw new RuntimeException("Game is over");

        Cell from = board.getCell(fromRow, fromCol);
        Cell to = board.getCell(toRow, toCol);
        Player player = getCurrentPlayer();

        if (from.isEmpty() || from.getPiece().getColor() != player.getColor()) {
            throw new RuntimeException("Pick your own piece");
        }
        if (!isValidMove(from, to)) throw new RuntimeException("Invalid move");

        to.setPiece(from.getPiece());
        from.setPiece(null);

        turn = 1 - turn;
        updateStatus();
    }

    // all cells the piece at (row, col) can legally go to
    public List<Cell> getValidMoves(int row, int col) {
        Cell from = board.getCell(row, col);
        List<Cell> valid = new ArrayList<>();
        if (from.isEmpty()) return valid;
        for (Cell to : from.getPiece().getPossibleMoves(board, from)) {
            if (isKingSafeAfterMove(from, to)) valid.add(to);
        }
        return valid;
    }

    private boolean isValidMove(Cell from, Cell to) {
        return from.getPiece().canMove(board, from, to) && isKingSafeAfterMove(from, to);
    }

    // try the move, check own king, put everything back
    private boolean isKingSafeAfterMove(Cell from, Cell to) {
        Piece piece = from.getPiece();
        Piece captured = to.getPiece();
        to.setPiece(piece);
        from.setPiece(null);
        boolean safe = !isInCheck(piece.getColor());
        from.setPiece(piece);
        to.setPiece(captured);
        return safe;
    }

    private boolean isInCheck(Color color) {
        Cell king = board.findKing(color);
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                Cell cell = board.getCell(r, c);
                Piece p = cell.getPiece();
                if (p != null && p.getColor() != color && p.canMove(board, cell, king)) return true;
            }
        }
        return false;
    }

    private boolean hasAnyValidMove(Color color) {
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                Cell cell = board.getCell(r, c);
                if (!cell.isEmpty() && cell.getPiece().getColor() == color && !getValidMoves(r, c).isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void updateStatus() {
        Color color = getCurrentPlayer().getColor();
        if (hasAnyValidMove(color)) return;
        status = isInCheck(color) ? GameStatus.CHECKMATE : GameStatus.STALEMATE;
    }

    public Player getCurrentPlayer() { return players[turn]; }
    public Player getWinner() { return status == GameStatus.CHECKMATE ? players[1 - turn] : null; }
    public GameStatus getStatus() { return status; }
    public Board getBoard() { return board; }
}
