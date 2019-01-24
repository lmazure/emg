package fr.mazure.maven.emg;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;


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
        
        try {
            final XWPFDocument docx = new XWPFDocument(new FileInputStream("H:\\Documents\\tmp\\SPEC_GENERIQUE_Fichier de base.odt"));
            final XWPFWordExtractor we = new XWPFWordExtractor(docx);
            System.out.println(we.getText());
        } catch (final IOException e) {
            e.printStackTrace();
        }
        
        //using XWPFWordExtractor Class
        
    }
}
