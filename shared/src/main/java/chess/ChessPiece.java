package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.List;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final PieceType type;
    private final ChessGame.TeamColor pieceColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        if (type == PieceType.KING) {
            /*
            * Checks all possible moves for a king and adds them to the list if they are empty or have an enemy piece
            * Does check the king's starting position, however this will never add
            * his current spot as he is always his own color.
            */
            int newRow = row;
            int newCol = col;
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    newRow = row + i;
                    newCol = col + j;
                    if (newRow > 0 && newRow < 9 && newCol > 0 && newCol < 9) {
                        ChessPosition newPosition = new ChessPosition(newRow, newCol);
                        ChessPiece occupyingPiece = board.getPiece(newPosition);
                        if (occupyingPiece == null || occupyingPiece.getTeamColor() != pieceColor) {
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        }
                    }
                }
            }

        } else if (type == PieceType.QUEEN || type == PieceType.ROOK) {
            // Handles orthogonal movement

            // Backward movement
            for (int i = row - 1; i > 0; i--) {
                ChessPosition newPosition = new ChessPosition(i, col);
                ChessPiece occupyingPiece = board.getPiece(newPosition);
                if (occupyingPiece == null || occupyingPiece.getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                } else {
                    break; // Stop looking further if it runs into its own piece
                }
            }
            // Forward movement
            for (int i = row + 1; i < 9; i++) {
                ChessPosition newPosition = new ChessPosition(i, col);
                ChessPiece occupyingPiece = board.getPiece(newPosition);
                if (occupyingPiece == null || occupyingPiece.getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                } else {
                    break; // Stop looking further if it runs into its own piece
                }
            }
            // Left-side movement
            for (int i = col - 1; i > 0; i--) {
                ChessPosition newPosition = new ChessPosition(row, i);
                ChessPiece occupyingPiece = board.getPiece(newPosition);
                if (occupyingPiece == null || occupyingPiece.getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                } else {
                    break; // Stop looking further if it runs into its own piece
                }
            }
            // Right-side movement
            for (int i = col + 1; i < 9; i++) {
                ChessPosition newPosition = new ChessPosition(row, i);
                ChessPiece occupyingPiece = board.getPiece(newPosition);
                if (occupyingPiece == null || occupyingPiece.getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                } else {
                    break; // Stop looking further if it runs into its own piece
                }
            }

        } else if (type == PieceType.QUEEN || type == PieceType.BISHOP) {
            // Handles diagonal movement

            // Used to track if a friendly piece has been encountered in each direction
            boolean isBlockedFrontLeft = false;
            boolean isBlockedFrontRight = false;
            boolean isBlockedBackLeft = false;
            boolean isBlockedBackRight = false;

            for (int i = 1; i < 8; i++) {
                // Front Right movement
                int newRow = row + i;
                int newCol = col + i;
                if (newRow > 8 || newCol > 8) { isBlockedBackLeft = true; }
                if (!isBlockedFrontRight) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    ChessPiece occupyingPiece = board.getPiece(newPosition);
                    if (occupyingPiece == null || occupyingPiece.getTeamColor() != pieceColor) {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    } else {
                        isBlockedFrontRight = true;
                    }
                } else if (!isBlockedFrontLeft) { // Front Left movement
                    newRow = row + i;
                    newCol = col - i;
                    if (newRow > 8 || newCol < 1) { isBlockedBackLeft = true; } else {
                        ChessPosition newPosition = new ChessPosition(newRow, newCol);
                        ChessPiece occupyingPiece = board.getPiece(newPosition);
                        if (occupyingPiece == null || occupyingPiece.getTeamColor() != pieceColor) {
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        } else {
                            isBlockedFrontLeft = true;
                        }
                    }
                } else if (!isBlockedBackRight) { // Back Right movement
                    newRow = row - i;
                    newCol = col + i;
                    if (newRow < 1 || newCol > 8) { isBlockedBackLeft = true; } else {
                        ChessPosition newPosition = new ChessPosition(newRow, newCol);
                        ChessPiece occupyingPiece = board.getPiece(newPosition);
                        if (occupyingPiece == null || occupyingPiece.getTeamColor() != pieceColor) {
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        } else {
                            isBlockedBackRight = true;
                        }
                    }
                } else if (!isBlockedBackLeft) {
                    newRow = row - i;
                    newCol = col - i;
                    if (newRow < 1 || newCol < 1) { isBlockedBackLeft = true; } else {
                        ChessPosition newPosition = new ChessPosition(newRow, newCol);
                        ChessPiece occupyingPiece = board.getPiece(newPosition);
                        if (occupyingPiece == null || occupyingPiece.getTeamColor() != pieceColor) {
                            moves.add(new ChessMove(myPosition, newPosition, null));
                        } else {
                            isBlockedBackLeft = true;
                        }
                    }
                }
            }
        } else if (type == PieceType.KNIGHT) {
            // Handles L-shaped movement
            int newRow = row + 2;
            int newCol = col + 1;
            if (newRow < 9) {
                if (newCol < 9) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    ChessPiece occupyingPiece = board.getPiece(newPosition);
                    if (occupyingPiece == null || occupyingPiece.getTeamColor() != pieceColor) {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
                newCol = col - 1;
                if (newCol > 0) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    ChessPiece occupyingPiece = board.getPiece(newPosition);
                    if (occupyingPiece == null || occupyingPiece.getTeamColor() != pieceColor) {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
            }
            newRow = row - 2;
            if (newRow > 0) {
                if (newCol > 0) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    ChessPiece occupyingPiece = board.getPiece(newPosition);
                    if (occupyingPiece == null || occupyingPiece.getTeamColor() != pieceColor) {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
                newCol = col + 1;
                if (newCol < 9) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    ChessPiece occupyingPiece = board.getPiece(newPosition);
                    if (occupyingPiece == null || occupyingPiece.getTeamColor() != pieceColor) {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
            }
            newCol = col + 2;
            newRow = row + 1;
            if (newCol < 9) {
                if (newRow < 9) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    ChessPiece occupyingPiece = board.getPiece(newPosition);
                    if (occupyingPiece == null || occupyingPiece.getTeamColor() != pieceColor) {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
                newRow = row - 1;
                if (newRow > 0) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    ChessPiece occupyingPiece = board.getPiece(newPosition);
                    if (occupyingPiece == null || occupyingPiece.getTeamColor() != pieceColor) {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
            }
            newCol = col - 2;
            if (newCol > 0) {
                if (newRow > 0) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    ChessPiece occupyingPiece = board.getPiece(newPosition);
                    if (occupyingPiece == null || occupyingPiece.getTeamColor() != pieceColor) {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
                newRow = row + 1;
                if (newRow < 9) {
                    ChessPosition newPosition = new ChessPosition(newRow, newCol);
                    ChessPiece occupyingPiece = board.getPiece(newPosition);
                    if (occupyingPiece == null || occupyingPiece.getTeamColor() != pieceColor) {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
            }
        } else { // Only remaining piece is a PAWN
            // Single move forward, single attack diagonal, optional double move forward if still on first rank
            int newRow = row;
            if (pieceColor == ChessGame.TeamColor.WHITE) {
                if (row == 2) {
                    newRow = row + 2;
                    ChessPosition newPosition = new ChessPosition(newRow, col);
                    ChessPiece occupyingPiece = board.getPiece(newPosition);
                    if (occupyingPiece == null) {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }
                }
                newRow = row + 1;
            } else {
                if (row == 7) {
                    newRow = row - 2;
                    ChessPosition newPosition = new ChessPosition(newRow, col);
                    ChessPiece occupyingPiece = board.getPiece(newPosition);
                    if (occupyingPiece == null) {
                        moves.add(new ChessMove(myPosition, newPosition, null));
                    }

                    newRow = row - 1;
                }
            }
            for (int i = -1; i < 2; i++) {
                int newCol = col + i;
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                ChessPiece occupyingPiece = board.getPiece(newPosition);
                if (occupyingPiece == null || (i != 0 && occupyingPiece.getTeamColor() != pieceColor)) {
                    moves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }

        return moves;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

    ChessPiece that = (ChessPiece) obj;
        return (pieceColor.equals(that.pieceColor) && type.equals(that.type));
    }

    @Override
    public int hashCode() {
        return 31 * Objects.hashCode(toString());
    }

    @Override
    public String toString() {
        return String.format("%s %s", pieceColor.toString(), type.toString()); // %s inserts a string value, %n inserts a newline
    }
}
