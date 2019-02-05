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
        assertEquals("source Id 'A' is duplicated:\n- location tt\n- location zz", analysis.getErrors().get(0));
        assertEquals("source Id 'B' is duplicated:\n- location uu\n- location ww\n- location yy", analysis.getErrors().get(1));
    }

    @Test
    void duplicatedTargetIdIsReported() {
        
        final List<SourceElement> sources = new ArrayList<SourceElement>();
        final List<BackwardTraceability> targetTraceabilities  = new ArrayList<BackwardTraceability>();
        final TraceabilityAnalyzer analyzer = new TraceabilityAnalyzer();

        targetTraceabilities.add(new BackwardTraceability(new TargetElement("A", "location A"), new ArrayList<String>()));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("B", "location D"), new ArrayList<String>()));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("C", "location C"), new ArrayList<String>()));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("A", "location D"), new ArrayList<String>()));
        final Analysis analysis = analyzer.analyze(sources, targetTraceabilities);
        
        assertEquals(0, analysis.getForwardTraceability().size());
        assertEquals(1, analysis.getErrors().size());
        assertEquals("target Id 'A' is duplicated:\n- location A\n- location D", analysis.getErrors().get(0));
    }

    @Test
    void triplicatedTargetIdIsReported() {
        
        final List<SourceElement> sources = new ArrayList<SourceElement>();
        final List<BackwardTraceability> targetTraceabilities  = new ArrayList<BackwardTraceability>();
        final TraceabilityAnalyzer analyzer = new TraceabilityAnalyzer();

        targetTraceabilities.add(new BackwardTraceability(new TargetElement("A", "location E"), new ArrayList<String>()));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("B", "location D"), new ArrayList<String>()));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("C", "location C"), new ArrayList<String>()));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("A", "location B"), new ArrayList<String>()));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("A", "location A"), new ArrayList<String>()));
        final Analysis analysis = analyzer.analyze(sources, targetTraceabilities);
        
        assertEquals(0, analysis.getForwardTraceability().size());
        assertEquals(1, analysis.getErrors().size());
        assertEquals("target Id 'A' is duplicated:\n- location A\n- location B\n- location E", analysis.getErrors().get(0));
    }
    

    @Test
    void twoDuplicatedTargetIdsAreReported() {
        
        final List<SourceElement> sources = new ArrayList<SourceElement>();
        final List<BackwardTraceability> targetTraceabilities  = new ArrayList<BackwardTraceability>();
        final TraceabilityAnalyzer analyzer = new TraceabilityAnalyzer();

        targetTraceabilities.add(new BackwardTraceability(new TargetElement("A", "location E"), new ArrayList<String>()));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("B", "location D"), new ArrayList<String>()));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("B", "location C"), new ArrayList<String>()));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("A", "location B"), new ArrayList<String>()));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("A", "location A"), new ArrayList<String>()));
        final Analysis analysis = analyzer.analyze(sources, targetTraceabilities);
        
        assertEquals(0, analysis.getForwardTraceability().size());
        assertEquals(2, analysis.getErrors().size());
        assertEquals("target Id 'A' is duplicated:\n- location A\n- location B\n- location E", analysis.getErrors().get(0));
        assertEquals("target Id 'B' is duplicated:\n- location C\n- location D", analysis.getErrors().get(1));
    }
    
    @Test
    void multipleDuplicatedSourceTargetIdsAreReported() {
        
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
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("A", "location E"), new ArrayList<String>()));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("B", "location D"), new ArrayList<String>()));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("B", "location C"), new ArrayList<String>()));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("A", "location B"), new ArrayList<String>()));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("A", "location A"), new ArrayList<String>()));
        final Analysis analysis = analyzer.analyze(sources, targetTraceabilities);
        
        assertEquals(0, analysis.getForwardTraceability().size());
        assertEquals(4, analysis.getErrors().size());
        assertEquals("source Id 'A' is duplicated:\n- location tt\n- location zz", analysis.getErrors().get(0));
        assertEquals("source Id 'B' is duplicated:\n- location uu\n- location ww\n- location yy", analysis.getErrors().get(1));
        assertEquals("target Id 'A' is duplicated:\n- location A\n- location B\n- location E", analysis.getErrors().get(2));
        assertEquals("target Id 'B' is duplicated:\n- location C\n- location D", analysis.getErrors().get(3));
    }
}
