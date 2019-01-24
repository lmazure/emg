package fr.mazure.maven.emg;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;


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
@SuppressWarnings("deprecation")
public class App 
{
    public static void main( String[] args )
    {

        final String filename = "H:\\Documents\\tmp\\SPEC_GENERIQUE_Fichier de base.odt";
        
        try {

            final ZipFile zFile = new ZipFile(filename);
            System.out.println(zFile.getName());

            final ZipEntry contentFile = zFile.getEntry("content.xml");

            System.out.println(contentFile.getName());
            System.out.println(contentFile.getSize());
            //XMLReader xr = XMLReaderFactory.createXMLReader();
            //OdtDocumentContentHandler handler = new OdtDocumentContentHandler();
            //xr.setContentHandler(handler);

            //xr.parse(new InputSource(zFile.getInputStream(contentFile)));

        } catch (final Exception e) {

            e.printStackTrace();

        }
    }
}
