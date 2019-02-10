package fr.mazure.maven.emg;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import fr.mazure.maven.emg.traceability.Analysis;
import fr.mazure.maven.emg.traceability.AnalysisFormater;
import fr.mazure.maven.emg.traceability.TraceabilityAnalyzer;

/*
 * - add comments
 * - implement checTraceablity(List<Requirements>, List<UpwardTrace>) which return a List<DownwardTrace>)
 *   implement both makup and HTML printing of the traceability matrix 
 */

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(final String[] args) throws IOException
    {
        final Application app = new Application();
        app.parseCommandeLine(args);
        
        final TraceabilityAnalyzer analyzer = new TraceabilityAnalyzer("requirement", "test");
        final Analysis analysis = analyzer.analyze(app.parseSpecFiles(), app.parseTestFiles());
        
        final AnalysisFormater formatter = new AnalysisFormater();
        final File file = File.createTempFile("traceability", ".html");
        try (final OutputStream f = new FileOutputStream(file)) {
            formatter.format(f, analysis);
        }
        Desktop.getDesktop().browse(file.toURI());
    }
}
