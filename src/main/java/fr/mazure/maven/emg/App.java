package fr.mazure.maven.emg;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import fr.mazure.maven.emg.traceability.Analysis;
import fr.mazure.maven.emg.traceability.AnalysisFormater;
import fr.mazure.maven.emg.traceability.TraceabilityAnalyzer;

public class App 
{
    static final String _syntaxHelp = "syntax: java -jar emg.jar -spec <specFile1> <specFile2> ... -test  <testFile1> <testFile2> ..."; 
    final private List<File> _specFiles;
    final private List<File> _testFiles;

    @SuppressWarnings("unused")
    public static void main(final String[] args) throws IOException
    {
        new App(args);
    }
    
    private App(final String[] args) throws IOException {
        
        _specFiles = new ArrayList<File>();
        _testFiles = new ArrayList<File>();

        parseCommandeLine(args);
        
        final SpecFileData specData = new SpecFileData(_specFiles);
        final TestFileData testData = new TestFileData(_testFiles);
        final TraceabilityAnalyzer analyzer = new TraceabilityAnalyzer("spec", "test");
        final Analysis analysis = analyzer.analyze(specData.getSourceElements(), testData.getBackwardTraceabilities());
        
        final AnalysisFormater formatter = new AnalysisFormater();
        final File file = File.createTempFile("traceability", ".html");
        try (final OutputStream f = new FileOutputStream(file)) {
            formatter.format(f, "spec", "test", specData.getErrors(), testData.getErrors(), analysis);
        }
        Desktop.getDesktop().browse(file.toURI());
    }

    private void parseCommandeLine(final String[] args) {

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
                }
                list.add(new File(arg));
            }
        }
    }
}
