package fr.mazure.maven.emg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.mazure.maven.emg.table.Table;
import fr.mazure.maven.emg.traceability.SourceElement;

public class SpecFileParser {

    static public List<SourceElement> parseSpecFiles(final List<File> specFiles) {        

        final List<SourceElement> sourceeElements = new ArrayList<SourceElement>();
        
        for (File file: specFiles) {
            sourceeElements.addAll(parseSpecFile(file));
        }
        
        return sourceeElements;
    }

    static private List<SourceElement> parseSpecFile(final File file) {        

        final List<SourceElement> sourceElements = new ArrayList<SourceElement>();
        
        for (Table table: OdtTableExtractor.extract(file)) {
            if (isSpecTable(table)) {
                sourceElements.addAll(parseSpecTable(file, table));
            }
        }
        
        return sourceElements;
    }

    static private List<SourceElement> parseSpecTable(final File file, final Table table) {

        final List<SourceElement> sourceElements = new ArrayList<SourceElement>();
        
        for (int row = 1; row < table.getNumberOfRows(); row++ ) {
            if (!table.isCellMerged(0, row)) {
                final String location = "file='" + file.getAbsolutePath() + " table='" + table.getName() + "' row='" + (row + 1) + "'";
                sourceElements.add(new SourceElement(table.getCellContent(0, row), location));
            }
        }
        
        return sourceElements;
    }

    static private boolean isSpecTable(final Table table) {

        if (table.getNumberOfRows() < 2) return false;
        if (table.getNumberOfColumns() != 2) return false;
        if (table.isCellMerged(0, 0)) return false;
        if (table.isCellMerged(1, 0)) return false;
        if (!table.getCellContent(0, 0).trim().equals("Id")) return false;
        if (!table.getCellContent(1, 0).trim().equals("Exigence")) return false;
        
        return true;
    }
}
