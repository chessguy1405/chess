package chess;

import java.util.Objects;
import java.util.Map;

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
     * Converts row number of position to rank letter for chess notation:
     * 1-8 -> a-h
     * 
     *
     * @return a char corresponding to row number
     */
    public char getFile() {
        Map<Integer, Character> conversionTable = Map.of(1, 'a', 2, 'b', 3, 'c', 4, 'd', 5, 'e', 6, 'f', 7, 'g', 8, 'h');
        return conversionTable.get(getColumn());
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

        @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ChessPosition that = (ChessPosition) obj;
        return (row == that.row && column == that.column);

    }

    @Override
    public int hashCode() {
        return 31 * Objects.hashCode(String.format("%s%s", getFile(), getRow()));
    }

    /**    (non-Javadoc)
     * Chess coordinates (e.g. b5) for the position
     * 
     * @return a string in chess coordinate notation
     */
    @Override
    public String toString() {
        return String.format("%s%s", getFile(), getRow());
    }
}
