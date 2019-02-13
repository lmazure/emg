package fr.mazure.maven.emg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.mazure.maven.emg.table.Table;
import fr.mazure.maven.emg.traceability.BackwardTraceability;
import fr.mazure.maven.emg.traceability.TargetElement;

public class TestFileData {
    
    private final List<BackwardTraceability> _backwardTraceabilities;
    private final List<String> _errors;
    private static final Pattern _pattern = Pattern.compile(SpecFileData.SPEC_ID_REGEXP);

    public TestFileData(final List<File> testFiles) {        

        _backwardTraceabilities = new ArrayList<BackwardTraceability>();
        _errors = new ArrayList<String>();
        
        for (File file: testFiles) {
            parseTestFile(file);
        }
    }

    public List<BackwardTraceability> getBackwardTraceabilities() {
        return _backwardTraceabilities;
    }
    
    public List<String> getErrors() {
        return _errors;
    }
    
    private void parseTestFile(final File file) {        

        for (Table table: OdtTableExtractor.extract(file)) {
            if (isTestTable(table)) {
                parseTestTable(file, table);
            }
        }
    }

    private void parseTestTable(final File file, final Table table) {

        final List<String> sourceIds = new ArrayList<String>();
        
        final String testId = table.getCellContent(1, 0).trim();
        final TargetElement testElement = new TargetElement(testId, "file='" + file.getAbsolutePath() + " table='" + table.getName() + "'");

        final String sources = table.getCellContent(1, 1);
        final Matcher matcher = _pattern.matcher(sources);
        while (matcher.find()) {
            sourceIds.add(matcher.group());
        }
        
        _backwardTraceabilities.add(new BackwardTraceability(testElement, sourceIds));
    }

    static private boolean isTestTable(final Table table) {

        if (table.getNumberOfRows() < 3) return false;
        if ((table.getNumberOfColumns() != 2) && (table.getNumberOfColumns() != 3)) return false; // sometimes the table has three columns with a merges cells on each row
        if (table.isCellMerged(0, 0)) return false;
        if (table.isCellMerged(1, 0)) return false;
        if (table.isCellMerged(0, 1)) return false;
        if (table.isCellMerged(1, 1)) return false;
        if (!table.getCellContent(0, 0).trim().equals("Id")) return false;
        if (!table.getCellContent(0, 1).trim().equals("Exigences")) return false;
        
        return true;
    }
}
