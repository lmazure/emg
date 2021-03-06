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

import fr.mazure.maven.emg.table.Table;

public class OdtTableExtractor {

    static final DocumentBuilder _builder = createDocumentBuilder();

    static public List<Table> extract(final File file) {

        final Document document = extractXmlContent(file);
        
        return extractTables(document);
    }

    private static Document extractXmlContent(final File file)
    {
        final String contentFilename = "content.xml";

        Document document = null;

        try (final ZipFile zipFile = new ZipFile(file)) {
            final ZipEntry contentFile = zipFile.getEntry(contentFilename);
            try {
                document = _builder.parse(new InputSource(zipFile.getInputStream(contentFile)));
                return document;
            } catch (final SAXException se){
                System.err.println("Failed to parse the XML file");
                se.printStackTrace();
                System.exit(1);
            } catch (final IOException ioe){
                System.err.println("Failed to read the XML file");
                ioe.printStackTrace();
                System.exit(1);
            }
        } catch (final Exception e) {
            System.err.println("Failed to extract '" + contentFilename + "': " + e);
            e.printStackTrace();
            System.exit(1);
        }
        
        // unreachable
        return null;
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
            final Element element = (Element)list.item(i); 
            if (!isInTable(element) && !isTrackedDeletion(element)) {
                tableList.add(extractTable((Element)list.item(i)));
            }
        }
        
        return tableList;
    }
    
    private static Table extractTable(final Element node) {

        // get name of the table
        final String tableName = node.getAttributeNode("table:name").getValue();

        // get the number of rows and the number of columns of the table
        int numberOfColumns = 0;
        int numberOfRows = 0;
        boolean hasHeaderRow = false;
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
            } else if (child.getNodeName().equals("table:table-row")) {
                numberOfRows++;
            } else if (child.getNodeName().equals("table:table-header-rows")) {
                numberOfRows++;
                hasHeaderRow = true;
            }
        }
        
        // create the table
        final Table table = new Table(numberOfColumns, numberOfRows, tableName, hasHeaderRow);

        // fill the table
        int rowNumber = 0;
        for (int i = 0; i < children.getLength(); i++) {
            final Element child = (Element)children.item(i);
            if (child.getNodeName().equals("table:table-row")) {
                extractRow(table, rowNumber, child);
                rowNumber++;
            } else if (child.getNodeName().equals("table:table-header-rows")) {
                final NodeList greatChildren = child.getChildNodes();
                boolean headerRowHasBeenSeen = false;
                for (int j = 0; j < greatChildren.getLength(); j++) {
                    final Element greatChild = (Element)greatChildren.item(j);
                    if (greatChild.getNodeName().equals("table:table-row")) {
                        if (headerRowHasBeenSeen) {
                            throw new InternalError("two <table:table-row> in <table:table-header-rows>");
                        }
                        extractRow(table, rowNumber, greatChild);
                        rowNumber++;
                        headerRowHasBeenSeen = true;
                    }
                }
            }
        }
        
        return table;
    }

    private static void extractRow(final Table table, int rowNumber, final Element node) {
        int columnNumber = 0;
        final NodeList children = node.getChildNodes();
        for (int j = 0; j < children.getLength(); j++) {
            final Element child = (Element)children.item(j);
            if (child.getNodeName().equals("table:table-cell")) {
                
                // cell value
                final String content = child.getTextContent();
                table.setCellContent(columnNumber, rowNumber, content);
                
                // cell merge
                final Attr numberRowsSpanned = child.getAttributeNode("table:number-rows-spanned");
                final Attr numberColumnsSpanned = child.getAttributeNode("table:number-columns-spanned");
                final int rowsSpanned = (numberRowsSpanned != null) ? Integer.parseInt(numberRowsSpanned.getValue()) : 1;
                final int columnsSpanned = (numberColumnsSpanned != null) ? Integer.parseInt(numberColumnsSpanned.getValue()) : 1;
                if ((rowsSpanned !=1) || (columnsSpanned != 1)) {
                    table.setCellMerge(columnNumber, rowNumber, columnsSpanned, rowsSpanned);
                }
            }
            columnNumber++;
        }
    }
    
    private static boolean isInTable(final Element node) {
        
        return isInContainingNode(node, "table:table");
    }

    private static boolean isTrackedDeletion(final Element node) {
        
        return isInContainingNode(node, "text:tracked-changes");
    }

    private static boolean isInContainingNode(final Element node, final String containingNodeName) {
        
        if (node.getParentNode() instanceof Document) return false;
        
        final Element parent = (Element)node.getParentNode();
        
        if (parent.getNodeName().equals(containingNodeName)) return true;
        
        return isInContainingNode(parent, containingNodeName);
    }


}
