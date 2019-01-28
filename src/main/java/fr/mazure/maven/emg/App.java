package fr.mazure.maven.emg;

import java.io.File;

/* 
 * POI does not work
 * let's look at
 * https://www.codeproject.com/Articles/38425/How-to-Read-and-Write-ODF-ODS-Files-OpenDocument-2
 * https://stackoverflow.com/questions/4962102/parse-document-structure-with-java
 * 
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
