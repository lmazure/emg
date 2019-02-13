package fr.mazure.maven.emg.table;

public class Table {

    final private String _tableName;
    final private int _numberOfColumns;
    final private int _numberOfRows;
    final private Object[][] _content;
    final private boolean _hasHeaderRow;
    
    /**
     * Create a Table of given size and name
     * @param numberOfColumns
     * @param numberOfRows
     * @param tableName
     * @param hasHeaderRow
     */
    public Table(final int numberOfColumns, final int numberOfRows, final String tableName, final boolean hasHeaderRow) {
        
        if (numberOfColumns <= 0) throw new IllegalArgumentException("numberOfColumns non positive (" + numberOfColumns + ")");
        if (numberOfRows <= 0) throw new IllegalArgumentException("numberOfRows non positive (" + numberOfRows + ")");

        _tableName = tableName;
        _numberOfColumns = numberOfColumns;
        _numberOfRows = numberOfRows;
        _content = new Object[numberOfColumns][numberOfRows];
        _hasHeaderRow = hasHeaderRow;
    }
    
    public void setCellContent(final int column, final int row, final String content) {

        if (column < 0) throw new IllegalArgumentException("column negative (" + column + ")");
        if (row < 0) throw new IllegalArgumentException("row negative (" + row + ")");
        if (column >= _numberOfColumns) throw new IllegalArgumentException("column too large (" + column + ")");
        if (row >= _numberOfRows) throw new IllegalArgumentException("row too large (" + column + ")");
        if (content == null) throw new IllegalArgumentException("content is null");

        setCell(column, row, content);
    }

    public void setCellContent(final CellLocation location, final String content) {
        
        setCellContent(location.getColumn(), location.getRow(), content);
    }

    public void setCellMerge(final int column, final int row, final int numberOfColumns, final int numberOfRows) {
        
        if (column < 0) throw new IllegalArgumentException("column negative (" + column + ")");
        if (row < 0) throw new IllegalArgumentException("row negative (" + row + ")");
        if (numberOfColumns <= 0) throw new IllegalArgumentException("numberOfColumns non positive (" + numberOfColumns + ")");
        if (numberOfRows <= 0) throw new IllegalArgumentException("numberOfRows non positive (" + numberOfRows + ")");
        if ((column + numberOfColumns) > _numberOfColumns) throw new IllegalArgumentException("column + numberOfColumns larger than total number of columns (" + column + "+" + numberOfColumns + ")");
        if ((row + numberOfRows) > _numberOfRows) throw new IllegalArgumentException("row + numberOfRows larger than total number of rows (" + row + "+" + numberOfRows + ")");
        
        boolean isFirstCell = true;
        final CellLocation location = new CellLocation(column, row);
        for (int c = 0; c < numberOfColumns; c++ ) {
            for (int r = 0; r < numberOfRows; r++ ) {
                if (!isFirstCell) {
                    setCell(column + c, row + r, location);
                }
                isFirstCell = false;
            }
        }
    }

    public void setCellMerge(final CellLocation location, final int numberOfColumns, final int numberOfRows) {

        setCellMerge(location.getColumn(), location.getRow(), numberOfColumns, numberOfRows);
    }
    
    private void setCell(final int column, final int row, final Object value) {

        if (column < 0) throw new IllegalArgumentException("column negative (" + column + ")");
        if (row < 0) throw new IllegalArgumentException("row negative (" + row + ")");
        if (column >= _numberOfColumns) throw new IllegalArgumentException("column too large (" + column + ")");
        if (row >= _numberOfRows) throw new IllegalArgumentException("row too large (" + column + ")");

        if (_content[column][row] != null) throw new IllegalArgumentException("cell (" + column + "," + row + ") has already been initialized");
            
        _content[column][row] = value;
    }

    public String getName() {
        return _tableName;
    }

    public int getNumberOfColumns() {
        return _numberOfColumns;
    }

    public int getNumberOfRows() {
        return _numberOfRows;
    }

    public String getCellContent(final int column, final int row) {

        if (column < 0) throw new IllegalArgumentException("column negative (" + column + ")");
        if (row < 0) throw new IllegalArgumentException("row negative (" + row + ")");
        if (column >= _numberOfColumns) throw new IllegalArgumentException("column too large (" + column + ")");
        if (row >= _numberOfRows) throw new IllegalArgumentException("row too large (" + column + ")");

        if (_content[column][row] == null) throw new IllegalStateException("cell (" + column + "," + row + ") has not been initialized");
        if (_content[column][row] instanceof CellLocation) throw new IllegalStateException("cell (" + column + "," + row + ") is merged");
        
        return (String)_content[column][row];
    }

    public String getCellContent(final CellLocation location) {
        
        return getCellContent(location.getColumn(), location.getRow());
    }

    public CellLocation getCellMerge(final int column, final int row) {

        if (column < 0) throw new IllegalArgumentException("column negative (" + column + ")");
        if (row < 0) throw new IllegalArgumentException("row negative (" + row + ")");
        if (column >= _numberOfColumns) throw new IllegalArgumentException("column too large (" + column + ")");
        if (row >= _numberOfRows) throw new IllegalArgumentException("row too large (" + column + ")");

        if (_content[column][row] == null) throw new IllegalStateException("cell (" + column + "," + row + ") has not been initialized");
        if (_content[column][row] instanceof String) throw new IllegalStateException("cell (" + column + "," + row + ") is not merged");
        
        return (CellLocation)_content[column][row];
    }

    public CellLocation getCellMerge(final CellLocation location) {
        
        return getCellMerge(location.getColumn(), location.getRow());
    }

    public boolean isCellMerged(final int column, final int row) {

        if (column < 0) throw new IllegalArgumentException("column negative (" + column + ")");
        if (row < 0) throw new IllegalArgumentException("row negative (" + row + ")");
        if (column >= _numberOfColumns) throw new IllegalArgumentException("column too large (" + column + ")");
        if (row >= _numberOfRows) throw new IllegalArgumentException("row too large (" + column + ")");

        if (_content[column][row] == null) throw new IllegalStateException("cell (" + column + "," + row + ") has not been initialized");
        
        return _content[column][row] instanceof CellLocation;
    }

    public CellLocation isCellMerged(final CellLocation location) {
        
        return getCellMerge(location.getColumn(), location.getRow());
    }
    
    public boolean hasHeaderRow() {
        return _hasHeaderRow;
    }
}
