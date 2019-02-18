package fr.mazure.maven.emg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.mazure.maven.emg.table.Table;
import fr.mazure.maven.emg.traceability.SourceElement;

public class SpecFileData {

    final private List<SourceElement> _sourceElements;
    final private List<String> _errors;
    final static public String SPEC_ID_REGEXP = "[-A-Za-z0-9_]+";
    
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
                final String specId = table.getCellContent(0, row);
                final String requirement = table.getCellContent(1, row);
                if (specId.isEmpty()) {
                    if (isCommentExigence(requirement)) {
                        // row containing a comment
                    } else if (requirement.isEmpty()) {
                        _errors.add("empty row in file='" + file.getAbsolutePath() + "' table='" + table.getName() + "' row='" + (row + 1) + "'");                        
                    } else {
                        _errors.add("empty spec Id table='" + table.getName() + "' row='" + (row + 1) + "'");                                                
                    }
                } else if (!specId.matches(SPEC_ID_REGEXP)) {
                    _errors.add("invalid spec Id '" + specId + "' in file='" + file.getAbsolutePath() + "' table='" + table.getName() + "' row='" + (row + 1) + "'");
                } else {
                    final String location = "file='" + file.getAbsolutePath() + " table='" + table.getName() + "' row='" + (row + 1) + "'";
                    _sourceElements.add(new SourceElement(specId, location));
                }
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
    
    private boolean isCommentExigence(final String requirement) {
        
        return requirement.matches("(?i:note\\h:.*)") || requirement.matches("(?i:com\\h:.*)");
    }
}
