package fr.mazure.maven.emg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.mazure.maven.emg.table.Table;
import fr.mazure.maven.emg.traceability.SourceElement;

public class SpecFileData {

    final private List<SourceElement> _sourceElements;
    final private List<String> _errors;
    
    public SpecFileData(final List<File> specFiles) {        

        _sourceElements = new ArrayList<SourceElement>();
        _errors = new ArrayList<String>();
        
        for (File file: specFiles) {
            parseSpecFile(file);
        }
    }

    public List<SourceElement> getSourceElements() {
        return _sourceElements;
    }
    
    public List<String> getErrors() {
        return _errors;
    }
    
    private void parseSpecFile(final File file) {        

        for (Table table: OdtTableExtractor.extract(file)) {
            if (isSpecTable(table)) {
                parseSpecTable(file, table);
            }
        }
    }

    private void parseSpecTable(final File file, final Table table) {

        if (table.getNumberOfRows() == 1 ) {
            _errors.add("Table '" + table.getName() + "' has only a header row");
            return;
        }

        for (int row = 1; row < table.getNumberOfRows(); row++ ) {
            if (!table.isCellMerged(0, row)) {
                final String location = "file='" + file.getAbsolutePath() + " table='" + table.getName() + "' row='" + (row + 1) + "'";
                _sourceElements.add(new SourceElement(table.getCellContent(0, row), location));
            }
        }
    }

    private boolean isSpecTable(final Table table) {

        if (table.getNumberOfColumns() != 2) return false;
        if (table.isCellMerged(0, 0)) return false;
        if (table.isCellMerged(1, 0)) return false;
        if (!table.getCellContent(0, 0).trim().equals("Id")) return false;
        if (!table.getCellContent(1, 0).trim().equals("Exigence")) return false;
        
        return true;
    }
}
