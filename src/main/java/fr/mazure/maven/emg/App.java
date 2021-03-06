package fr.mazure.maven.emg;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import fr.mazure.maven.emg.traceability.Analysis;
import fr.mazure.maven.emg.traceability.AnalysisFormatter;
import fr.mazure.maven.emg.traceability.TargetListReportFormatter;
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
        
        final AnalysisFormatter analysisformatter = new AnalysisFormatter("spec", "test");
        final File analysisFile = File.createTempFile("traceability", ".html");
        try (final OutputStream f = new FileOutputStream(analysisFile)) {
            analysisformatter.format(f, specData.getErrors(), testData.getErrors(), analysis);
        }
        displayUriInBrowser(analysisFile.toURI());

        final TargetListReportFormatter testReportformatter =
            new TargetListReportFormatter("test", new String[] { "itération 1", "itération 2", "itération 3", "bug ID", "commentaire" });
        final File testReportFile = File.createTempFile("test-report", ".html");
        try (final OutputStream f = new FileOutputStream(testReportFile)) {
            testReportformatter.format(f, testData.getBackwardTraceabilities());
        }
        displayUriInBrowser(testReportFile.toURI());

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
    
    static private void displayUriInBrowser(final URI uri) throws IOException {
        
        Desktop.getDesktop().browse(uri);
    }
}
