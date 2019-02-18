package fr.mazure.maven.emg;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class SpecFileDataTest {

    @Test
    void test() {
        
        final File file = new File("testdata/SpecFile.odt");
        final SpecFileData data = new SpecFileData(Arrays.asList(file));
        
        assertEquals(2, data.getSourceElements().size());
        assertEquals("ID_A", data.getSourceElements().get(0).getId());
        assertEquals("file='" + file.getAbsolutePath() + "' table='Tableau1' row='2'", data.getSourceElements().get(0).getLocation());
        assertEquals("ID_B", data.getSourceElements().get(1).getId());
        assertEquals("file='" + file.getAbsolutePath() + "' table='Tableau1' row='8'", data.getSourceElements().get(1).getLocation());
        assertEquals(3, data.getErrors().size());
        assertEquals("empty row in file='" + file.getAbsolutePath() + "' table='Tableau1' row='3'", data.getErrors().get(0));
        assertEquals("empty spec Id in file='" + file.getAbsolutePath() + "' table='Tableau1' row='9'", data.getErrors().get(1));
        assertEquals("invalid spec Id 'This row should be reported has an error.' in file='" + file.getAbsolutePath() + "' table='Tableau1' row='10'", data.getErrors().get(2));
    }

}
