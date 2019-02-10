package fr.mazure.maven.emg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.mazure.maven.emg.table.Table;
import fr.mazure.maven.emg.traceability.BackwardTraceability;
import fr.mazure.maven.emg.traceability.TargetElement;

public class TestFileParser {

    static public List<BackwardTraceability> parseTestFiles(final List<File> testFiles) {        

        final List<BackwardTraceability> backwardTraceabilities = new ArrayList<BackwardTraceability>();
        
        for (File file: testFiles) {
            backwardTraceabilities.addAll(parseTestFile(file));
        }
        
        return backwardTraceabilities;
    }

    static private List<BackwardTraceability> parseTestFile(final File file) {        

        final List<BackwardTraceability> backwardTraceabilities = new ArrayList<BackwardTraceability>();
        
        for (Table table: OdtTableExtractor.extract(file)) {
            if (isTestTable(table)) {
                backwardTraceabilities.add(parseTestTable(file, table));
            }
        }
        
        return backwardTraceabilities;
    }

    static private BackwardTraceability parseTestTable(final File file, final Table table) {

        final List<String> sourceIds = new ArrayList<String>();
        
        final String testId = table.getCellContent(1, 0).trim();
        final TargetElement testElement = new TargetElement(testId, "file='" + file.getAbsolutePath() + " table='" + table.getName() + "'");

        final String sources = table.getCellContent(1, 1);
        final Pattern pattern = Pattern.compile("[-A-Za-z0-9_]+");
        final Matcher matcher = pattern.matcher(sources);
        while (matcher.find()) {
            sourceIds.add(matcher.group());
        }
        
        return new BackwardTraceability(testElement, sourceIds);
    }

    static private boolean isTestTable(final Table table) {

        if (table.getNumberOfRows() < 3) return false;
        if ((table.getNumberOfColumns() != 2) && (table.getNumberOfColumns() != 3)) return false; // sometime the table has three columns with a merges cells on each row
        if (table.isCellMerged(0, 0)) return false;
        if (table.isCellMerged(1, 0)) return false;
        if (table.isCellMerged(0, 1)) return false;
        if (table.isCellMerged(1, 1)) return false;
        if (!table.getCellContent(0, 0).trim().equals("Id")) return false;
        if (!table.getCellContent(0, 1).trim().equals("Exigences")) return false;
        
        return true;
    }
}
