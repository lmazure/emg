package fr.mazure.maven.emg.table;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.mazure.maven.emg.OdtTableExtractor;
import fr.mazure.maven.emg.table.CellLocation;
import fr.mazure.maven.emg.table.Table;
import fr.mazure.maven.emg.table.TableExtractor;

class OdtTableExtractorTest {

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

    @Test
    void manageMergedCells() {
        final TableExtractor extractor = new OdtTableExtractor();
        List<Table> list = extractor.extract(new File("testdata/TablesWithMergedCells.odt"));

        assertEquals(3, list.size());
        
        assertEquals("Tableau1", list.get(0).getName());
        assertEquals(2, list.get(0).getNumberOfRows());
        assertEquals(2, list.get(0).getNumberOfColumns());
        assertEquals(false, list.get(0).isCellMerged(0, 0));  assertEquals("1", list.get(0).getCellContent(0, 0));
        assertEquals(true, list.get(0).isCellMerged(1, 0));   assertEquals(new CellLocation(0, 0), list.get(0).getCellMerge(1, 0));
        assertEquals(false, list.get(0).isCellMerged(0, 1));  assertEquals("2", list.get(0).getCellContent(0, 1));
        assertEquals(false, list.get(0).isCellMerged(1, 1));  assertEquals("3", list.get(0).getCellContent(1, 1));

        assertEquals("Tableau2", list.get(1).getName());
        assertEquals(2, list.get(1).getNumberOfRows());
        assertEquals(2, list.get(1).getNumberOfColumns());
        assertEquals(false, list.get(1).isCellMerged(0, 0));  assertEquals("1", list.get(1).getCellContent(0, 0));
        assertEquals(false, list.get(1).isCellMerged(1, 0));  assertEquals("2", list.get(1).getCellContent(1, 0));
        assertEquals(true, list.get(1).isCellMerged(0, 1));   assertEquals(new CellLocation(0, 0), list.get(1).getCellMerge(0, 1));
        assertEquals(false, list.get(1).isCellMerged(1, 1));  assertEquals("3", list.get(1).getCellContent(1, 1));
        
        assertEquals("Tableau3", list.get(2).getName());
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
}
