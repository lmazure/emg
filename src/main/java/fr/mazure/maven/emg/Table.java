package fr.mazure.maven.emg;

public class Table {

    final private String _tableName;
    final private int _numberOfColumns;
    final private int _numberOfRows;
    final private String[][] _content;
    
    public Table(final int numberOfColumns, final int numberOfRows, final String tableName) {
        
        if (numberOfColumns <= 0) throw new IllegalArgumentException("numberOfColumns non positive (" + numberOfColumns + ")");
        if (numberOfRows <= 0) throw new IllegalArgumentException("numberOfRows non positive (" + numberOfRows + ")");

        _tableName = tableName;
        _numberOfColumns = numberOfColumns;
        _numberOfRows = numberOfRows;
        _content = new String[numberOfColumns][numberOfRows];
    }
    
    public void setCellContent(final int column, final int row, final String content) {
        
        _content[column][row] = content;
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
        return _content[column][row];
    }
}
