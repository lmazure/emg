package fr.mazure.maven.emg.table;

public class CellLocation {

    final int _row;
    final int _column;
    
    /**
     * @param column
     * @param row
     */
    public CellLocation(final int column, final int row) {
        if (column < 0) throw new IllegalArgumentException("column non positive (" + column + ")");
        if (row < 0) throw new IllegalArgumentException("row non positive (" + row + ")");

        _row = row;
        _column = column;
    }

    /**
     * @return row
     */
    public int getRow() {
        return _row;
    }

    /**
     * @return column
     */
    public int getColumn() {
        return _column;
    }

    /*
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return _row * 12345 + _column; 
    }

    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof CellLocation)) return false;
        
        final CellLocation other = (CellLocation) obj;
        return ((_column == other._column) && (_row == other._row)); 
    }

    /*
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CellLocation [column=" + _column + ", row=" + _row + "]";
    }
}
