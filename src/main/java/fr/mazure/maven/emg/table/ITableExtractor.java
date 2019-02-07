package fr.mazure.maven.emg.table;

import java.io.File;
import java.util.List;

public interface ITableExtractor {

    public List<Table> extract(final File file);
}
