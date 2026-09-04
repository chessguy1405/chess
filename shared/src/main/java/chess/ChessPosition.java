package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    // These represent the actual indices, not user-friendly values.
    private int row;
    private int column;

    public ChessPosition(int row, int col) {
        this.row = row - 1;
        this.column = col - 1;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.row + 1;
    }

    /**
     * @return which array row index this position is in
     * 0 codes for the bottom row
     */
    public int getRowIndex() {
        return this.row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left column
     */
    public int getColumn() {
        return this.column + 1;
    }

    /**
     * @return the array column index this position is in
     * 0 codes for the left column
     */
    public int getColumnIndex() {
        return this.column;
    }
}
