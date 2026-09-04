package chess;

import chess.ChessPiece;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    var board;

    public ChessBoard() {
        board = new ChessPiece[8][8]; // Create an empty 2D array (8x8) to represent the board itself
        resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        throw new RuntimeException("Not implemented");
    }

    private void resetBackRow(TeamColor color) {
        int rank;
        if (color == WHITE) {
            rank = 0;
        } else {
            rank = 7;
        }

        board[rank][0] = new ChessPiece(color, ROOK);
        board[rank][1] = new ChessPiece(color, KNIGHT);
        board[rank][2] = new ChessPiece(color, BISHOP);
        board[rank][3] = new ChessPiece(color, QUEEN);
        board[rank][4] = new ChessPiece(color, KING);
        board[rank][5] = new ChessPiece(color, BISHOP);
        board[rank][6] = new ChessPiece(color, KNIGHT);
        board[rank][7] = new ChessPiece(color, ROOK);
    }

    private void resetFrontRow(TeamColor color) {
        int rank;
        if (color == WHITE) {
            rank = 1;
        } else {
            rank = 6;
        }
        for (int i = 0; i < 8; i++) {
            board[rank][i] = new ChessPiece(color, PAWN);
        }
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // Set the back rows for white and black
        resetBackRow(WHITE);
        resetBackRow(BLACK);

        //Set the front rows for white and black
        resetFrontRow(WHITE);
        resetFrontRow(BLACK);
    }
}
