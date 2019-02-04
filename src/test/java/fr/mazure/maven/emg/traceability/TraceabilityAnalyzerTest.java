package fr.mazure.maven.emg.traceability;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class TraceabilityAnalyzerTest {

    @Test
    void emptyList() {
        
        final List<SourceElement> sources = new ArrayList<SourceElement>();
        final List<BackwardTraceability> targetTraceabilities  = new ArrayList<BackwardTraceability>();
        final TraceabilityAnalyzer analyzer = new TraceabilityAnalyzer();
        
        final Analysis analysis = analyzer.analyze(sources, targetTraceabilities);
        
        assertEquals(0, analysis.getForwardTraceability().size());
        assertEquals(0, analysis.getErrors().size());
    }

}
