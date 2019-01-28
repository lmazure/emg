package fr.mazure.maven.emg;

import java.io.File;
import java.util.List;

public interface TableExtractor {

    public List<Table> extract(final File file);
}
