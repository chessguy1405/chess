package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    public ChessPosition startPosition;
    public ChessPosition endPosition;
    public ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

    ChessMove that = (ChessMove) obj;
        return (startPosition.equals(that.startPosition) && endPosition.equals(that.endPosition) && promotionPiece == that.promotionPiece);
    }

    @Override
    public int hashCode() {
        int result = 43; // Start with a non-zero, prime number to make order matter
        result += Objects.hashCode(startPosition);
        result *= 17; // Multiply by prime number to maintain the integrity of the order
        result += Objects.hashCode(endPosition);
        result *= 17;
        result += Objects.hashCode(promotionPiece);

        return result;
    }

    @Override
    public String toString() {
        return String.format("%s-%s",startPosition.toString(),endPosition.toString()); // %s inserts a string value, %n inserts a newline
    }
}
