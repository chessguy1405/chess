package ui;

import chess.*;

public class BoardRenderer {

    public static void printBoard(ChessGame game, ChessGame.TeamColor view) {
        ChessBoard board = game.getBoard();
        boolean whiteView = view == ChessGame.TeamColor.WHITE;

        for (int rowOffset = 0; rowOffset < 8; rowOffset++) {
            int rank = whiteView ? 8 - rowOffset : rowOffset + 1;
            printLine(board, whiteView, rank);
        }
    }

    private static void printLine(ChessBoard board, boolean whiteView, int rank) {
        for (int colOffset = 0; colOffset < 8; colOffset++) {
            int file = whiteView ? colOffset + 1 : 8 - colOffset;
            boolean darkSquare = (rank + file) % 2 == 0;

            String background = darkSquare ? EscapeSequences.SET_BG_COLOR_DARK_GREY : EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
            ChessPiece piece = board.getPiece(new ChessPosition(rank, file));

            String textColor = darkSquare ? EscapeSequences.SET_TEXT_COLOR_DARK_GREY : EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;
            String invisiblePiece = textColor + EscapeSequences.WHITE_PAWN + EscapeSequences.RESET_TEXT_COLOR;
            String content = piece != null ? pieceSymbol(piece) : invisiblePiece;

            System.out.print(background + content + EscapeSequences.RESET_BG_COLOR);
        }

        System.out.println();
    }

    private static String pieceSymbol(ChessPiece piece) {
        if (piece == null) return EscapeSequences.EMPTY;

        return switch(piece.getTeamColor()) {
            case BLACK -> blackPieceSymbol(piece);
            case WHITE -> whitePieceSymbol(piece);
        };
    }

    private static String blackPieceSymbol(ChessPiece piece) {
        if (piece == null) return EscapeSequences.EMPTY;

        return switch(piece.getPieceType()) {
            case PAWN -> EscapeSequences.BLACK_PAWN;
            case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
            case BISHOP -> EscapeSequences.BLACK_BISHOP;
            case ROOK -> EscapeSequences.BLACK_ROOK;
            case QUEEN -> EscapeSequences.BLACK_QUEEN;
            case KING -> EscapeSequences.BLACK_KING;
        };
    }

    private static String whitePieceSymbol(ChessPiece piece) {
        if (piece == null) return EscapeSequences.EMPTY;

        return switch(piece.getPieceType()) {
            case PAWN -> EscapeSequences.WHITE_PAWN;
            case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
            case BISHOP -> EscapeSequences.WHITE_BISHOP;
            case ROOK -> EscapeSequences.WHITE_ROOK;
            case QUEEN -> EscapeSequences.WHITE_QUEEN;
            case KING -> EscapeSequences.WHITE_KING;
        };
    }
}
