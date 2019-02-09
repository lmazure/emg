package fr.mazure.maven.emg;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.mazure.maven.emg.table.ITableExtractor;
import fr.mazure.maven.emg.table.Table;
import fr.mazure.maven.emg.traceability.BackwardTraceability;
import fr.mazure.maven.emg.traceability.SourceElement;
import fr.mazure.maven.emg.traceability.TargetElement;

public class Application {

    static final String _syntaxHelp = "syntax: java -jar emg.jar -spec <specFile1> <specFile2> ... -test  <testFile1> <testFile2> ..."; 
    final private List<File> _specFiles;
    final private List<File> _testFiles;
    final private ITableExtractor _tableExtractor;
    
    /**
     *
     */
    public Application() {
        _specFiles = new ArrayList<File>();
        _testFiles = new ArrayList<File>();
        _tableExtractor = new OdtTableExtractor();
    }

    public void parseCommandeLine(final String[] args) {

        List<File> list = null;
        
        for (String arg: args) {
            if (arg.startsWith("-")) {
                if (arg.equals("-spec")) {
                    list = _specFiles;
                } else if (arg.equals("-test")) {
                    list = _testFiles;
                } else {
                    throw new IllegalArgumentException("Illegal flag on the command line (" + _syntaxHelp + ")");
                }
            } else {
                if (list == null) {
                    throw new IllegalArgumentException("Missing flag on the command line (" + _syntaxHelp + ")");                    
                } else {
                    list.add(new File(arg));
                }
            }
        }
    }
    
    public List<SourceElement> parseSpecFiles() {        

        final List<SourceElement> sourceeElements = new ArrayList<SourceElement>();
        
        for (File file: _specFiles) {
            sourceeElements.addAll(parseSpecFile(file));
        }
        
        return sourceeElements;
    }

    private List<SourceElement> parseSpecFile(final File file) {        

        final List<SourceElement> sourceElements = new ArrayList<SourceElement>();
        
        for (Table table: _tableExtractor.extract(file)) {
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

    public List<BackwardTraceability> parseTestFiles() {        

        final List<BackwardTraceability> backwardTraceabilities = new ArrayList<BackwardTraceability>();
        
        for (File file: _testFiles) {
            backwardTraceabilities.addAll(parseTestFile(file));
        }
        
        return backwardTraceabilities;
    }

    private List<BackwardTraceability> parseTestFile(final File file) {        

        final List<BackwardTraceability> backwardTraceabilities = new ArrayList<BackwardTraceability>();
        
        for (Table table: _tableExtractor.extract(file)) {
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
        if (table.getNumberOfColumns() != 2) return false;
        if (table.isCellMerged(0, 0)) return false;
        if (table.isCellMerged(1, 0)) return false;
        if (table.isCellMerged(0, 1)) return false;
        if (table.isCellMerged(1, 1)) return false;
        if (!table.getCellContent(0, 0).trim().equals("Id")) return false;
        if (!table.getCellContent(0, 1).trim().equals("Exigences")) return false;
        
        return true;
    }
}
