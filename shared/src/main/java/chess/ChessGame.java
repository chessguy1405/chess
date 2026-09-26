package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.ArrayList;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor teamTurn;
    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    private void passTurn() {
        if (teamTurn == TeamColor.BLACK) {
            teamTurn = TeamColor.WHITE;
        } else {
            teamTurn = TeamColor.BLACK;
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece movingPiece = board.getPiece(startPosition);
        if (movingPiece == null) {
            System.out.println("No Piece Present!");
            return null;
        } else {
            System.out.println(movingPiece.toString());
            Collection<ChessMove> possibleMoves = movingPiece.pieceMoves(board, startPosition);
            Collection<ChessMove> allowedMoves = new ArrayList<ChessMove>(possibleMoves);
            ChessBoard realBoard = new ChessBoard(board);
            for (ChessMove move : possibleMoves) {
                ChessBoard testBoard = new ChessBoard(board);
                testBoard.addPiece(move.getEndPosition(), movingPiece);
                testBoard.removePiece(move.getStartPosition());
                if (isInCheck(teamTurn)) {
                    allowedMoves.remove(move);
                }
                board = realBoard;
            }
            if (allowedMoves.isEmpty()) {
                System.out.println("Piece present, but no valid moves!");
                return null;
            } else {
                return allowedMoves;
            }
        }
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> goodMoves = validMoves(move.getStartPosition());
        ChessPiece movingPiece = board.getPiece(move.getStartPosition());
        // Collection<ChessMove> goodMoves = movingPiece.pieceMoves(board, move.getStartPosition());

        if (movingPiece == null) {
            System.out.println("No piece present when move made!");
        } else {
            System.out.println(movingPiece.toString());
        }
        if (goodMoves == null) {
            System.out.println(move.toString());
            throw new InvalidMoveException("No valid moves from this square");
        } else if (!goodMoves.contains(move)) {
            System.out.println(move.toString());
            throw new InvalidMoveException("Invalid move, moves available.");
        }

        if (move.getPromotionPiece() != null) {
            movingPiece = new ChessPiece(movingPiece.getTeamColor(), move.getPromotionPiece());
            if (movingPiece == null) {
                System.out.println("No piece present when promoting!");
            } else {
                System.out.println(movingPiece.toString());
            }
        }


        board.removePiece(move.getStartPosition());
        if (board.getPiece(move.getStartPosition()) != null) {
            System.out.println(board.getPiece(move.getEndPosition()).toString());
        } else { System.out.println("No Piece at Start Position!"); }
        board.addPiece(move.getEndPosition(), movingPiece);
        if (board.getPiece(move.getEndPosition()) != null) {
            System.out.println(board.getPiece(move.getEndPosition()).toString());
        } else { System.out.println("No Piece at End Position!"); }
        System.out.println();
        passTurn();
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingSquare = null;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition square = new ChessPosition(i, j);
                ChessPiece occupyingPiece = board.getPiece(square);
                if (occupyingPiece != null && occupyingPiece.getTeamColor() == teamColor && occupyingPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    kingSquare = square;
                    break;
                }
            }
            if (kingSquare != null) {
                break; // exit the outer loop if the king has been found already
            }
        }
        if (kingSquare == null) {
            return false;
        }
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition square = new ChessPosition(i, j);
                ChessPiece occupyingPiece = board.getPiece(square);
                if (occupyingPiece != null && occupyingPiece.getTeamColor() != teamColor) {
                    Collection<ChessMove> pieceMoves = occupyingPiece.pieceMoves(board, square);
                    for (ChessMove move : pieceMoves) {
                        if (move.getEndPosition().equals(kingSquare)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean canMove(TeamColor teamColor) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition square = new ChessPosition(i, j);
                ChessPiece occupyingPiece = board.getPiece(square);
                if (occupyingPiece != null && occupyingPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> possibleMoves = validMoves(square);
                    if (possibleMoves != null) {
                        return true; // Return false if at least one legal move is found
                    }
                }
            }
        }
        return false; // Return false if all pieces on the board of that color cannot move
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return (isInCheck(teamColor) && !canMove(teamColor));
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return (!isInCheck(teamColor) && !canMove(teamColor));
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

    ChessGame that = (ChessGame) obj;
        return board.equals(that.board) && this.teamTurn == that.teamTurn;
    }

    @Override
    public int hashCode() {
        int result = 31 * Objects.hashCode(this.board);
        result += Objects.hashCode(this.teamTurn);
        return result;
    }

    @Override
    public String toString() {
        return String.format("Turn: %s%nBoard State:%n%s", this.teamTurn, this.board);
    }
}
