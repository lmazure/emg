package fr.mazure.maven.emg;

public class CellLocation {

    final int _row;
    final int _column;
    
    public CellLocation(final int row, final int column) {

        if (column <= 0) throw new IllegalArgumentException("column non positive (" + column + ")");
        if (row <= 0) throw new IllegalArgumentException("row non positive (" + row + ")");

        _row = row;
        _column = column;
    }

    public int getRow() {
        return _row;
    }

    public int getColumn() {
        return _column;
    }
}
