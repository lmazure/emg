package fr.mazure.maven.emg;

import java.io.File;

import fr.mazure.maven.emg.table.TableExtractor;

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
    public static void main( String[] args )
    {

        final String filename = "H:\\Documents\\tmp\\SPEC_GENERIQUE_Fichier de base.odt";
        
        final TableExtractor extractor = new OdtTableExtractor();
        extractor.extract(new File(filename));
    }
}
