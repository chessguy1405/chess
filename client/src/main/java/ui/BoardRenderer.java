package ui;

import chess.ChessGame;
import chess.ChessPiece;

public class BoardRenderer {

    public static void printBoard(ChessGame game, ChessGame.TeamColor view) {

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
