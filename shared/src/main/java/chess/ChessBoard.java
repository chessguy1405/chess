package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece[][] board;

    public ChessBoard() {
        board = new ChessPiece[8][8]; // Create an empty 2D array (8x8) to represent the board itself
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRowIndex()][position.getColumnIndex()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRowIndex()][position.getColumnIndex()];
    }

    /**
     * Removes a piece from the chessboard
     * 
     * @param position Where to remove the piece from
     */
    public void removePiece(ChessPosition position) {
        board[position.getRowIndex()][position.getColumnIndex()] = null;
    }

    /**
     * Resets the back row of a color to RNBQKBNR
     * 
     * @param color The color/team you are resetting the back row for
     */
    private void resetBackRow(ChessGame.TeamColor color) {
        int rank;
        if (color == ChessGame.TeamColor.WHITE) {
            rank = 0;
        } else {
            rank = 7;
        }

        board[rank][0] = new ChessPiece(color, ChessPiece.PieceType.ROOK);
        board[rank][1] = new ChessPiece(color, ChessPiece.PieceType.KNIGHT);
        board[rank][2] = new ChessPiece(color, ChessPiece.PieceType.BISHOP);
        board[rank][3] = new ChessPiece(color, ChessPiece.PieceType.QUEEN);
        board[rank][4] = new ChessPiece(color, ChessPiece.PieceType.KING);
        board[rank][5] = new ChessPiece(color, ChessPiece.PieceType.BISHOP);
        board[rank][6] = new ChessPiece(color, ChessPiece.PieceType.KNIGHT);
        board[rank][7] = new ChessPiece(color, ChessPiece.PieceType.ROOK);
    }

    /**
     * Resets the front row (pawns) of a color
     * 
     * @param color The color you are resetting the pawns for
     */
    private void resetFrontRow(ChessGame.TeamColor color) {
        int rank;
        if (color == ChessGame.TeamColor.WHITE) {
            rank = 1;
        } else {
            rank = 6;
        }
        for (int i = 0; i < 8; i++) {
            board[rank][i] = new ChessPiece(color, ChessPiece.PieceType.PAWN);
        }
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // Set the back rows for white and black
        resetBackRow(ChessGame.TeamColor.WHITE);
        resetBackRow(ChessGame.TeamColor.BLACK);

        // Set the front rows for white and black
        resetFrontRow(ChessGame.TeamColor.WHITE);
        resetFrontRow(ChessGame.TeamColor.BLACK);

        // Set all other rows to empty squares
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = null;
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

    ChessBoard that = (ChessBoard) obj;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        int result = 43; // Give a non-zero, prime constant to help prevent collisions based on empty initial squares
        /*
         * Iteratively generates a unique hash based on the state of each square on the board.
         * Order-dependent (using prime factors and non-zero constant to start)
         * Runs from left to right, bottom to top (assuming 0,0 is the bottom left corner as will be displayed)
         */
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = getPiece(position);
                result *= 17;
                if (piece != null) {
                    result += Objects.hashCode(piece);
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return String.format("This is filler text");
    }
}
