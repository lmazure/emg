package fr.mazure.maven.emg;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

class OdtTableExtractorTest extends OdtTableExtractor {

    @Test
    void canExtractOneTable() {
        final TableExtractor extractor = new OdtTableExtractor();
        List<Table> list = extractor.extract(new File("testdata/OneTableWithOne1x1Cell.odt"));

        assertEquals(1, list.size());
        assertEquals("TableOne", list.get(0).getName());
        assertEquals(1, list.get(0).getNumberOfRows());
        assertEquals(1, list.get(0).getNumberOfColumns());
        assertEquals("Cell 1-1", list.get(0).getCellContent(0, 0));
    }

    @Test
    void canExtractThreeTable() {
        final TableExtractor extractor = new OdtTableExtractor();
        List<Table> list = extractor.extract(new File("testdata/ThreeTables2x3_4x1_1x5.odt"));

        assertEquals(3, list.size());
        
        assertEquals("Table1", list.get(0).getName());
        assertEquals(3, list.get(0).getNumberOfRows());
        assertEquals(2, list.get(0).getNumberOfColumns());
        assertEquals("a", list.get(0).getCellContent(0, 0));
        assertEquals("b", list.get(0).getCellContent(1, 0));
        assertEquals("c", list.get(0).getCellContent(0, 1));
        assertEquals("d", list.get(0).getCellContent(1, 1));
        assertEquals("e", list.get(0).getCellContent(0, 2));
        assertEquals("f", list.get(0).getCellContent(1, 2));

        assertEquals("Table2", list.get(1).getName());
        assertEquals(4, list.get(1).getNumberOfRows());
        assertEquals(1, list.get(1).getNumberOfColumns());
        assertEquals("g", list.get(1).getCellContent(0, 0));
        assertEquals("h", list.get(1).getCellContent(0, 1));
        assertEquals("i", list.get(1).getCellContent(0, 2));
        assertEquals("j", list.get(1).getCellContent(0, 3));

        assertEquals("Table3", list.get(2).getName());
        assertEquals(1, list.get(2).getNumberOfRows());
        assertEquals(5, list.get(2).getNumberOfColumns());
        assertEquals("k", list.get(2).getCellContent(0, 0));
        assertEquals("l", list.get(2).getCellContent(1, 0));
        assertEquals("m", list.get(2).getCellContent(2, 0));
        assertEquals("n", list.get(2).getCellContent(3, 0));
        assertEquals("o", list.get(2).getCellContent(4, 0));
    }

    @Test
    void ignoreTableInTable() {
        final TableExtractor extractor = new OdtTableExtractor();
        List<Table> list = extractor.extract(new File("testdata/TableInTable.odt"));

        assertEquals(1, list.size());
        assertEquals("Table1", list.get(0).getName());
        assertEquals(2, list.get(0).getNumberOfRows());
        assertEquals(2, list.get(0).getNumberOfColumns());
        assertEquals("a", list.get(0).getCellContent(0, 0));
        assertEquals("b", list.get(0).getCellContent(1, 0));
        assertEquals("c", list.get(0).getCellContent(0, 1));
        assertEquals("defg", list.get(0).getCellContent(1, 1));
    }

}
