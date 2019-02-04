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

    @Test
    void duplicatedSourceIdIsReported() {
        
        final List<SourceElement> sources = new ArrayList<SourceElement>();
        final List<BackwardTraceability> targetTraceabilities  = new ArrayList<BackwardTraceability>();
        final TraceabilityAnalyzer analyzer = new TraceabilityAnalyzer();
        
        sources.add(new SourceElement("A", "location A"));
        sources.add(new SourceElement("B", "location B"));
        sources.add(new SourceElement("A", "location C"));
        final Analysis analysis = analyzer.analyze(sources, targetTraceabilities);
        
        assertEquals(0, analysis.getForwardTraceability().size());
        assertEquals(1, analysis.getErrors().size());
        assertEquals("source Id 'A' is duplicated:\n- location A\n- location C", analysis.getErrors().get(0));
    }

    @Test
    void triplicatedSourceIdIsReported() {
        
        final List<SourceElement> sources = new ArrayList<SourceElement>();
        final List<BackwardTraceability> targetTraceabilities  = new ArrayList<BackwardTraceability>();
        final TraceabilityAnalyzer analyzer = new TraceabilityAnalyzer();
        
        sources.add(new SourceElement("A", "location zz"));
        sources.add(new SourceElement("B", "location yy"));
        sources.add(new SourceElement("D", "location xx"));
        sources.add(new SourceElement("B", "location ww"));
        sources.add(new SourceElement("E", "location vv"));
        sources.add(new SourceElement("B", "location uu"));
        sources.add(new SourceElement("F", "location tt"));
        final Analysis analysis = analyzer.analyze(sources, targetTraceabilities);
        
        assertEquals(0, analysis.getForwardTraceability().size());
        assertEquals(1, analysis.getErrors().size());
        assertEquals("source Id 'B' is duplicated:\n- location uu\n- location ww\n- location yy", analysis.getErrors().get(0));
    }

    @Test
    void twoDuplicatedSourceIdAreReported() {
        
        final List<SourceElement> sources = new ArrayList<SourceElement>();
        final List<BackwardTraceability> targetTraceabilities  = new ArrayList<BackwardTraceability>();
        final TraceabilityAnalyzer analyzer = new TraceabilityAnalyzer();
        
        sources.add(new SourceElement("A", "location zz"));
        sources.add(new SourceElement("B", "location yy"));
        sources.add(new SourceElement("D", "location xx"));
        sources.add(new SourceElement("B", "location ww"));
        sources.add(new SourceElement("E", "location vv"));
        sources.add(new SourceElement("B", "location uu"));
        sources.add(new SourceElement("A", "location tt"));
        final Analysis analysis = analyzer.analyze(sources, targetTraceabilities);
        
        assertEquals(0, analysis.getForwardTraceability().size());
        assertEquals(2, analysis.getErrors().size());
        assertEquals("source Id 'A' is duplicated:\n- location tt\n- location zz", analysis.getErrors().get(0)); // TODO this test may fal, the order of errors is undefined !
        assertEquals("source Id 'B' is duplicated:\n- location uu\n- location ww\n- location yy", analysis.getErrors().get(1));
    }

}
