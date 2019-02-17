package fr.mazure.maven.emg.table;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.mazure.maven.emg.OdtTableExtractor;
import fr.mazure.maven.emg.table.CellLocation;
import fr.mazure.maven.emg.table.Table;

class OdtTableExtractorTest {

    @Test
    void canExtractOneTable() {
        List<Table> list = OdtTableExtractor.extract(new File("testdata/OneTableWithOne1x1Cell.odt"));

        assertEquals(1, list.size());
        assertEquals("TableOne", list.get(0).getName());
        assertFalse(list.get(0).hasHeaderRow());
        assertEquals(1, list.get(0).getNumberOfRows());
        assertEquals(1, list.get(0).getNumberOfColumns());
        assertEquals("Cell 1-1", list.get(0).getCellContent(0, 0));
    }

    @Test
    void canExtractThreeTable() {
        List<Table> list = OdtTableExtractor.extract(new File("testdata/ThreeTables2x3_4x1_1x5.odt"));

        assertEquals(3, list.size());
        
        assertEquals("Table1", list.get(0).getName());
        assertFalse(list.get(0).hasHeaderRow());
        assertEquals(3, list.get(0).getNumberOfRows());
        assertEquals(2, list.get(0).getNumberOfColumns());
        assertEquals("a", list.get(0).getCellContent(0, 0));
        assertEquals("b", list.get(0).getCellContent(1, 0));
        assertEquals("c", list.get(0).getCellContent(0, 1));
        assertEquals("d", list.get(0).getCellContent(1, 1));
        assertEquals("e", list.get(0).getCellContent(0, 2));
        assertEquals("f", list.get(0).getCellContent(1, 2));

        assertEquals("Table2", list.get(1).getName());
        assertFalse(list.get(1).hasHeaderRow());
        assertEquals(4, list.get(1).getNumberOfRows());
        assertEquals(1, list.get(1).getNumberOfColumns());
        assertEquals("g", list.get(1).getCellContent(0, 0));
        assertEquals("h", list.get(1).getCellContent(0, 1));
        assertEquals("i", list.get(1).getCellContent(0, 2));
        assertEquals("j", list.get(1).getCellContent(0, 3));

        assertEquals("Table3", list.get(2).getName());
        assertFalse(list.get(2).hasHeaderRow());
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
        List<Table> list = OdtTableExtractor.extract(new File("testdata/TableInTable.odt"));

        assertEquals(1, list.size());
        assertEquals("Table1", list.get(0).getName());
        assertFalse(list.get(0).hasHeaderRow());
        assertEquals(2, list.get(0).getNumberOfRows());
        assertEquals(2, list.get(0).getNumberOfColumns());
        assertEquals("a", list.get(0).getCellContent(0, 0));
        assertEquals("b", list.get(0).getCellContent(1, 0));
        assertEquals("c", list.get(0).getCellContent(0, 1));
        assertEquals("defg", list.get(0).getCellContent(1, 1));
    }

    @Test
    void manageMergedCells() {
        List<Table> list = OdtTableExtractor.extract(new File("testdata/TablesWithMergedCells.odt"));

        assertEquals(3, list.size());
        
        assertEquals("Tableau1", list.get(0).getName());
        assertFalse(list.get(0).hasHeaderRow());
        assertEquals(2, list.get(0).getNumberOfRows());
        assertEquals(2, list.get(0).getNumberOfColumns());
        assertEquals(false, list.get(0).isCellMerged(0, 0));  assertEquals("1", list.get(0).getCellContent(0, 0));
        assertEquals(true, list.get(0).isCellMerged(1, 0));   assertEquals(new CellLocation(0, 0), list.get(0).getCellMerge(1, 0));
        assertEquals(false, list.get(0).isCellMerged(0, 1));  assertEquals("2", list.get(0).getCellContent(0, 1));
        assertEquals(false, list.get(0).isCellMerged(1, 1));  assertEquals("3", list.get(0).getCellContent(1, 1));

        assertEquals("Tableau2", list.get(1).getName());
        assertFalse(list.get(1).hasHeaderRow());
        assertEquals(2, list.get(1).getNumberOfRows());
        assertEquals(2, list.get(1).getNumberOfColumns());
        assertEquals(false, list.get(1).isCellMerged(0, 0));  assertEquals("1", list.get(1).getCellContent(0, 0));
        assertEquals(false, list.get(1).isCellMerged(1, 0));  assertEquals("2", list.get(1).getCellContent(1, 0));
        assertEquals(true, list.get(1).isCellMerged(0, 1));   assertEquals(new CellLocation(0, 0), list.get(1).getCellMerge(0, 1));
        assertEquals(false, list.get(1).isCellMerged(1, 1));  assertEquals("3", list.get(1).getCellContent(1, 1));
        
        assertEquals("Tableau3", list.get(2).getName());
        assertFalse(list.get(2).hasHeaderRow());
        assertEquals(5, list.get(2).getNumberOfRows());
        assertEquals(4, list.get(2).getNumberOfColumns());
        assertEquals(false, list.get(2).isCellMerged(0, 0));  assertEquals("1", list.get(2).getCellContent(0, 0));
        assertEquals(true, list.get(2).isCellMerged(1, 0));   assertEquals(new CellLocation(0, 0), list.get(2).getCellMerge(1, 0));
        assertEquals(false, list.get(2).isCellMerged(2, 0));  assertEquals("2", list.get(2).getCellContent(2, 0));
        assertEquals(false, list.get(2).isCellMerged(3, 0));  assertEquals("3", list.get(2).getCellContent(3, 0));
        assertEquals(true, list.get(2).isCellMerged(0, 1));   assertEquals(new CellLocation(0, 0), list.get(2).getCellMerge(0, 1));
        assertEquals(true, list.get(2).isCellMerged(1, 1));   assertEquals(new CellLocation(0, 0), list.get(2).getCellMerge(1, 1));
        assertEquals(false, list.get(2).isCellMerged(2, 1));  assertEquals("4", list.get(2).getCellContent(2, 1));
        assertEquals(true, list.get(2).isCellMerged(3, 1));   assertEquals(new CellLocation(2, 1), list.get(2).getCellMerge(3, 1));
        assertEquals(false, list.get(2).isCellMerged(0, 2));  assertEquals("5", list.get(2).getCellContent(0, 2));
        assertEquals(false, list.get(2).isCellMerged(1, 2));  assertEquals("6", list.get(2).getCellContent(1, 2));
        assertEquals(true, list.get(2).isCellMerged(2, 2));   assertEquals(new CellLocation(1, 2), list.get(2).getCellMerge(2, 2));
        assertEquals(true, list.get(2).isCellMerged(3, 2));   assertEquals(new CellLocation(1, 2), list.get(2).getCellMerge(3, 2));
        assertEquals(true, list.get(2).isCellMerged(0, 3));   assertEquals(new CellLocation(0, 2), list.get(2).getCellMerge(0, 3));
        assertEquals(false, list.get(2).isCellMerged(1, 3));  assertEquals("7", list.get(2).getCellContent(1, 3));
        assertEquals(false, list.get(2).isCellMerged(2, 3));  assertEquals("8", list.get(2).getCellContent(2, 3));
        assertEquals(false, list.get(2).isCellMerged(3, 3));  assertEquals("9", list.get(2).getCellContent(3, 3));
        assertEquals(true, list.get(2).isCellMerged(0, 4));   assertEquals(new CellLocation(0, 2), list.get(2).getCellMerge(0, 4));
        assertEquals(false, list.get(2).isCellMerged(1, 4));  assertEquals("10", list.get(2).getCellContent(1, 4));
        assertEquals(false, list.get(2).isCellMerged(2, 4));  assertEquals("11", list.get(2).getCellContent(2, 4));
        assertEquals(true, list.get(2).isCellMerged(3, 4));   assertEquals(new CellLocation(3, 3), list.get(2).getCellMerge(3, 4));       
    }
    
    @Test
    void canExtractOneTableWithHeader() {
        List<Table> list = OdtTableExtractor.extract(new File("testdata/TableWithHeader.odt"));

        assertEquals(1, list.size());
        assertEquals("Tab*1", list.get(0).getName());
        assertTrue(list.get(0).hasHeaderRow());
        assertEquals(3, list.get(0).getNumberOfRows());
        assertEquals(3, list.get(0).getNumberOfColumns());
        assertEquals("head_1", list.get(0).getCellContent(0, 0));
        assertEquals("head_2", list.get(0).getCellContent(1, 0));
        assertEquals("head_3", list.get(0).getCellContent(2, 0));
        assertEquals("A", list.get(0).getCellContent(0, 1));
        assertEquals("B", list.get(0).getCellContent(1, 1));
        assertEquals("C", list.get(0).getCellContent(2, 1));
        assertEquals("D", list.get(0).getCellContent(0, 2));
        assertEquals("E", list.get(0).getCellContent(1, 2));
        assertEquals("F", list.get(0).getCellContent(2, 2));
    }
    
    @Test
    void deletedTextIsIgnored() {
        List<Table> list = OdtTableExtractor.extract(new File("testdata/TableWithDeletedText.odt"));

        assertEquals(1, list.size());
        assertEquals("Tableau1", list.get(0).getName());
        assertFalse(list.get(0).hasHeaderRow());
        assertEquals(5, list.get(0).getNumberOfRows());
        assertEquals(3, list.get(0).getNumberOfColumns());
        assertEquals("X", list.get(0).getCellContent(0, 0));
        assertEquals("B", list.get(0).getCellContent(1, 0));
        assertEquals("C", list.get(0).getCellContent(2, 0));
        assertEquals("D", list.get(0).getCellContent(0, 1));
        assertEquals("E", list.get(0).getCellContent(1, 1));
        assertEquals("F", list.get(0).getCellContent(2, 1));
        assertEquals("", list.get(0).getCellContent(0, 2));
        assertEquals("", list.get(0).getCellContent(1, 2));
        assertEquals("", list.get(0).getCellContent(2, 2));
        assertEquals("J", list.get(0).getCellContent(0, 3));
        assertEquals("K", list.get(0).getCellContent(1, 3));
        assertEquals("L", list.get(0).getCellContent(2, 3));
        assertEquals("", list.get(0).getCellContent(0, 4));
        assertEquals("", list.get(0).getCellContent(1, 4));
        assertEquals("", list.get(0).getCellContent(2, 4));
    }
}
