package org.chessGame.model;

public class Board {
    public static final int SIZE = 8;
    private final Cell[][] cells = new Cell[SIZE][SIZE];

    public Board() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                cells[r][c] = new Cell(r, c);
            }
        }
        setupPieces(0, 1, Color.BLACK);
        setupPieces(7, 6, Color.WHITE);
    }

    private void setupPieces(int backRow, int pawnRow, Color color) {
        cells[backRow][0].setPiece(new Rook(color));
        cells[backRow][1].setPiece(new Knight(color));
        cells[backRow][2].setPiece(new Bishop(color));
        cells[backRow][3].setPiece(new Queen(color));
        cells[backRow][4].setPiece(new King(color));
        cells[backRow][5].setPiece(new Bishop(color));
        cells[backRow][6].setPiece(new Knight(color));
        cells[backRow][7].setPiece(new Rook(color));
        for (int c = 0; c < SIZE; c++) {
            cells[pawnRow][c].setPiece(new Pawn(color));
        }
    }

    public Cell getCell(int row, int col) {
        if (!isInside(row, col)) {
            throw new IllegalArgumentException("Cell out of board");
        }
        return cells[row][col];
    }

    public boolean isInside(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    public Cell findKing(Color color) {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                if (cell.getPiece() instanceof King && cell.getPiece().getColor() == color) return cell;
            }
        }
        throw new IllegalStateException("King not found");
    }

    public void printBoard() {
        for (Cell[] row : cells) {
            for (Cell cell : row) {
                Piece p = cell.getPiece();
                char ch = p == null ? '.' : p.getColor() == Color.WHITE ? p.getSymbol() : Character.toLowerCase(p.getSymbol());
                System.out.print(ch + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
