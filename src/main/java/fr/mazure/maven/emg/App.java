package fr.mazure.maven.emg;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.mazure.maven.emg.traceability.Analysis;
import fr.mazure.maven.emg.traceability.AnalysisFormater;
import fr.mazure.maven.emg.traceability.BackwardTraceability;
import fr.mazure.maven.emg.traceability.SourceElement;
import fr.mazure.maven.emg.traceability.TargetElement;
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
    public static void main( String[] args ) throws IOException
    {

        /*final String filename = "H:\\Documents\\tmp\\SPEC_GENERIQUE_Fichier de base.odt";
        
        final ITableExtractor extractor = new OdtTableExtractor();
        extractor.extract(new File(filename));*/
        
        
        final List<SourceElement> sources = new ArrayList<SourceElement>();
        final List<BackwardTraceability> targetTraceabilities  = new ArrayList<BackwardTraceability>();
        final TraceabilityAnalyzer analyzer = new TraceabilityAnalyzer();

        sources.add(new SourceElement("sC", "location sC"));
        sources.add(new SourceElement("sA", "location sA"));
        sources.add(new SourceElement("sB", "location sB"));
        sources.add(new SourceElement("sB", "location <a>&<b>"));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("tD", "location tD"), Arrays.asList("sE", "sB")));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("tA", "location tA"), Arrays.asList("sA")));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("tE", "location tE"), Arrays.asList("sC", "sF")));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("tB", "location tB"), Arrays.asList("sA", "sF", "sB")));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("tC", "location tC"), Arrays.asList("sA", "sB", "sC")));
        final Analysis analysis = analyzer.analyze(sources, targetTraceabilities);
        
        final AnalysisFormater formatter = new AnalysisFormater();
        final File file = File.createTempFile("traceability", ".html");
        try (final OutputStream f = new FileOutputStream(file)) {
            formatter.format(f, analysis);
        }
        Desktop.getDesktop().browse(file.toURI());

    }
}
