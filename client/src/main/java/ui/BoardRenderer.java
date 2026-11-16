package ui;

import chess.*;

public class BoardRenderer {

    public static void printBoard(ChessGame game, ChessGame.TeamColor view) {
        ChessBoard board = game.getBoard();
        boolean whiteView = view == ChessGame.TeamColor.WHITE;

        for (int rowOffset = 0; rowOffset < 8; rowOffset++) {
            int rank = whiteView ? 8 - rowOffset : rowOffset + 1;
            for (int squareLine = 0; squareLine < 3; squareLine++) {
                printLine(board, whiteView, rank, squareLine);
            }
        }
    }

    private static void printLine(ChessBoard board, boolean whiteView, int rank, int squareLine) {
        for (int colOffset = 0; colOffset < 8; colOffset++) {
            int file = whiteView ? colOffset + 1 : 8 - colOffset;
            boolean darkSquare = (rank + file) % 2 == 1;

            String background = darkSquare ? EscapeSequences.SET_BG_COLOR_DARK_GREY : EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
            ChessPiece piece = board.getPiece(new ChessPosition(rank, file));
            String content = squareLine == 1 ? pieceSymbol(piece) : EscapeSequences.EMPTY;

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
