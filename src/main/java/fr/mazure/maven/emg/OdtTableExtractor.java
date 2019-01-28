package fr.mazure.maven.emg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class OdtTableExtractor implements TableExtractor {

    public List<Table> extract(File file) {

        final Document document = extractXmlContent(file);
        
        return extractTables(document);
    }

    private static Document extractXmlContent(final File file)
    {
        final String contentfilename = "content.xml";

        DocumentBuilder builder = createDocumentBuilder();

        ZipFile zipFile = null;
        ZipEntry contentFile = null;
        try {
            zipFile = new ZipFile(file);
            contentFile = zipFile.getEntry(contentfilename);
        } catch (final Exception e) {
            System.err.println("Failed to extract '" + contentfilename + "': " + e);
            e.printStackTrace();
            System.exit(1);
        }        

        Document document = null;
        try {
            document = builder.parse(new InputSource(zipFile.getInputStream(contentFile)));
        } catch (final SAXException se){
            System.err.println("Failed to parse the XML file");
            se.printStackTrace();
            System.exit(1);
        } catch (final IOException ioe){
            System.err.println("Failed to read the XML file");
            ioe.printStackTrace();
            System.exit(1);
        }
        
        return document;
    }

    private static DocumentBuilder createDocumentBuilder()
    {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (final ParserConfigurationException e){
            System.err.println("Failed to configure the XML parser: " + e);
            e.printStackTrace();
            System.exit(1);
        }
        return builder;
    }
    
    private static List<Table> extractTables(final Document document) {

        final List<Table> tableList = new ArrayList<Table>();
        
        final Element racine = document.getDocumentElement();
        final NodeList list = racine.getElementsByTagName("table:table");

        for (int i = 0; i < list.getLength(); i++) {
            if (!isInTable((Element)list.item(i))) {
                tableList.add(extractTable((Element)list.item(i)));
            }
        }
        
        return tableList;
    }
    
    private static Table extractTable(final Element node) {

        final String tableName = node.getAttributeNode("table:name").getValue();

        int numberOfColumns = 0;
        int numberOfRows = 0;
        final NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++ ){
            final Element child = (Element)children.item(i);
            if (child.getNodeName().equals("table:table-column")) {
                final Attr numberColumnsRepeated = child.getAttributeNode("table:number-columns-repeated");
                if (numberColumnsRepeated != null) {
                    numberOfColumns += Integer.parseInt(numberColumnsRepeated.getValue());                    
                } else {
                    numberOfColumns++;
                }
            }
            if (child.getNodeName().equals("table:table-row")) numberOfRows++;
        }
        
        final Table table = new Table(numberOfColumns, numberOfRows, tableName);

        int rowNumber = 0;
        for (int i = 0; i < children.getLength(); i++) {
            final Element child = (Element)children.item(i);
            if (child.getNodeName().equals("table:table-row")) {
                int columnNumber = 0;
                final NodeList greatChildren = child.getChildNodes();
                for (int j = 0; j < greatChildren.getLength(); j++) {
                    final Element greatChild = (Element)greatChildren.item(j);
                    if (greatChild.getNodeName().equals("table:table-cell")) {
                        final String content = greatChild.getTextContent();
                        table.setCellContent(columnNumber, rowNumber, content);
                        columnNumber++;
                    }
                }
                rowNumber++;
            }
        }
        
        return table;
    }
    
    private static boolean isInTable(final Element node) {
        
        if (node.getParentNode() instanceof Document) return false;
        
        final Element parent = (Element)node.getParentNode();
        
        if (parent.getNodeName().equals("table:table")) return true;
        
        return isInTable(parent);
    }
}
