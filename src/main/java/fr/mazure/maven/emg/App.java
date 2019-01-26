package fr.mazure.maven.emg;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


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
        final String contentfilename = "content.xml";
        
        ZipFile zFile = null;
        ZipEntry contentFile = null;
        
        try {

            zFile = new ZipFile(filename);
            System.out.println(zFile.getName());

            contentFile = zFile.getEntry(contentfilename);

            System.out.println(contentFile.getName());
            System.out.println(contentFile.getSize());

        } catch (final Exception e) {
            System.err.println("Failed get '" + contentfilename + "': " + e);
            e.printStackTrace();
            System.exit(1);

        }
        
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = null;
        try{
            builder = factory.newDocumentBuilder();
        } catch (final ParserConfigurationException e){
            System.err.println("Failed to configure the XML parser: " + e);
            e.printStackTrace();
            System.exit(1);
        }

        Document document = null;
        try{
            document = builder.parse(new InputSource(zFile.getInputStream(contentFile)));
        } catch (final SAXException se){
            System.err.println("Failed to parse the XML file");
            se.printStackTrace();
            System.exit(1);
        } catch (final IOException ioe){
            System.err.println("Failed to read the XML file");
            ioe.printStackTrace();
            System.exit(1);
        }

        final Element racine = document.getDocumentElement();
        final NodeList list = racine.getElementsByTagName("table:table");

        for(int i=0; i<list.getLength(); i++){
            
            final Element articleNode = (Element)list.item(i);

            final String tableName = articleNode.getAttributeNode("table:name").getValue();

            System.out.println("tableau " + i + " : " + tableName);
        }
    }
}
