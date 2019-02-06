package fr.mazure.maven.emg.traceability;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
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
    void duplicatedSourceIdIsReportedAsError() {
        
        final List<SourceElement> sources = new ArrayList<SourceElement>();
        final List<BackwardTraceability> targetTraceabilities  = new ArrayList<BackwardTraceability>();
        final TraceabilityAnalyzer analyzer = new TraceabilityAnalyzer();
        
        sources.add(new SourceElement("A", "location A"));
        sources.add(new SourceElement("B", "location B"));
        sources.add(new SourceElement("A", "location C"));
        final Analysis analysis = analyzer.analyze(sources, targetTraceabilities);

        assertEquals(1, analysis.getErrors().size());
        assertEquals("source Id 'A' is duplicated:\n- location A\n- location C", analysis.getErrors().get(0));
    }

    @Test
    void triplicatedSourceIdIsReportedAsError() {
        
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

        assertEquals(1, analysis.getErrors().size());
        assertEquals("source Id 'B' is duplicated:\n- location uu\n- location ww\n- location yy", analysis.getErrors().get(0));
    }

    @Test
    void twoDuplicatedSourceIdAreReportedAsError() {
        
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
        

        assertEquals(2, analysis.getErrors().size());
        assertEquals("source Id 'A' is duplicated:\n- location tt\n- location zz", analysis.getErrors().get(0));
        assertEquals("source Id 'B' is duplicated:\n- location uu\n- location ww\n- location yy", analysis.getErrors().get(1));
    }

    @Test
    void duplicatedTargetIdIsReportedAsError() {
        
        final List<SourceElement> sources = new ArrayList<SourceElement>();
        final List<BackwardTraceability> targetTraceabilities  = new ArrayList<BackwardTraceability>();
        final TraceabilityAnalyzer analyzer = new TraceabilityAnalyzer();

        sources.add(new SourceElement("XXX", "location XXX"));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("A", "location A"), Arrays.asList("XXX")));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("B", "location D"), Arrays.asList("XXX")));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("C", "location C"), Arrays.asList("XXX")));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("A", "location D"), Arrays.asList("XXX")));
        final Analysis analysis = analyzer.analyze(sources, targetTraceabilities);
        
        assertEquals(1, analysis.getErrors().size());
        assertEquals("target Id 'A' is duplicated:\n- location A\n- location D", analysis.getErrors().get(0));
    }

    @Test
    void triplicatedTargetIdIsReportedAsError() {
        
        final List<SourceElement> sources = new ArrayList<SourceElement>();
        final List<BackwardTraceability> targetTraceabilities  = new ArrayList<BackwardTraceability>();
        final TraceabilityAnalyzer analyzer = new TraceabilityAnalyzer();

        sources.add(new SourceElement("XXX", "location XXX"));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("A", "location E"), Arrays.asList("XXX")));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("B", "location D"), Arrays.asList("XXX")));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("C", "location C"), Arrays.asList("XXX")));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("A", "location B"), Arrays.asList("XXX")));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("A", "location A"), Arrays.asList("XXX")));
        final Analysis analysis = analyzer.analyze(sources, targetTraceabilities);
        
        assertEquals(1, analysis.getErrors().size());
        assertEquals("target Id 'A' is duplicated:\n- location A\n- location B\n- location E", analysis.getErrors().get(0));
    }
    

    @Test
    void twoDuplicatedTargetIdsAreReportedAsError() {
        
        final List<SourceElement> sources = new ArrayList<SourceElement>();
        final List<BackwardTraceability> targetTraceabilities  = new ArrayList<BackwardTraceability>();
        final TraceabilityAnalyzer analyzer = new TraceabilityAnalyzer();

        sources.add(new SourceElement("XXX", "location XXX"));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("A", "location E"), Arrays.asList("XXX")));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("B", "location D"), Arrays.asList("XXX")));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("B", "location C"), Arrays.asList("XXX")));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("A", "location B"), Arrays.asList("XXX")));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("A", "location A"), Arrays.asList("XXX")));
        final Analysis analysis = analyzer.analyze(sources, targetTraceabilities);
        
        assertEquals(2, analysis.getErrors().size());
        assertEquals("target Id 'A' is duplicated:\n- location A\n- location B\n- location E", analysis.getErrors().get(0));
        assertEquals("target Id 'B' is duplicated:\n- location C\n- location D", analysis.getErrors().get(1));
    }
    
    @Test
    void multipleDuplicatedSourceTargetIdsAreReportedAsError() {
        
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
        sources.add(new SourceElement("XXX", "location XXX"));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("A", "location E"), Arrays.asList("XXX")));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("B", "location D"), Arrays.asList("XXX")));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("B", "location C"), Arrays.asList("XXX", "A", "A")));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("A", "location B"), Arrays.asList("XXX")));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("A", "location A"), Arrays.asList("XXX")));
        final Analysis analysis = analyzer.analyze(sources, targetTraceabilities);
        
        assertEquals(5, analysis.getErrors().size());
        assertEquals("source Id 'A' is duplicated:\n- location tt\n- location zz", analysis.getErrors().get(0));
        assertEquals("source Id 'B' is duplicated:\n- location uu\n- location ww\n- location yy", analysis.getErrors().get(1));
        assertEquals("target Id 'A' is duplicated:\n- location A\n- location B\n- location E", analysis.getErrors().get(2));
        assertEquals("target Id 'B' is duplicated:\n- location C\n- location D", analysis.getErrors().get(3));
        assertEquals("the backward tracebility of target Id 'B' contains a duplicated source Id: 'A'", analysis.getErrors().get(4));
    }

    @Test
    void emptyBackwardTraceabilityIsReportedAsError() {
        
        final List<SourceElement> sources = new ArrayList<SourceElement>();
        final List<BackwardTraceability> targetTraceabilities  = new ArrayList<BackwardTraceability>();
        final TraceabilityAnalyzer analyzer = new TraceabilityAnalyzer();

        sources.add(new SourceElement("sA", "location sA"));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("tA", "location tA"), new ArrayList<String>()));
        final Analysis analysis = analyzer.analyze(sources, targetTraceabilities);
        
        assertEquals(1, analysis.getErrors().size());
        assertEquals("target Id 'tA' has no backward traceability", analysis.getErrors().get(0));
    }

    @Test
    void duplicatedBackwardTraceabilityIsReportedAsError() {
        
        final List<SourceElement> sources = new ArrayList<SourceElement>();
        final List<BackwardTraceability> targetTraceabilities  = new ArrayList<BackwardTraceability>();
        final TraceabilityAnalyzer analyzer = new TraceabilityAnalyzer();

        sources.add(new SourceElement("sA", "location sA"));
        sources.add(new SourceElement("sB", "location sB"));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("tA", "location tA"), Arrays.asList("sA", "sB", "sA")));
        final Analysis analysis = analyzer.analyze(sources, targetTraceabilities);
        
        assertEquals(1, analysis.getErrors().size());
        assertEquals("the backward tracebility of target Id 'tA' contains a duplicated source Id: 'sA'", analysis.getErrors().get(0));
    }

    @Test
    void analyzeSingleTraceability() {
        
        final List<SourceElement> sources = new ArrayList<SourceElement>();
        final List<BackwardTraceability> targetTraceabilities  = new ArrayList<BackwardTraceability>();
        final TraceabilityAnalyzer analyzer = new TraceabilityAnalyzer();

        sources.add(new SourceElement("sA", "location sA"));
        targetTraceabilities.add(new BackwardTraceability(new TargetElement("tA", "location tA"), Arrays.asList("sA")));
        final Analysis analysis = analyzer.analyze(sources, targetTraceabilities);
        
        assertEquals(0, analysis.getErrors().size());
        assertEquals(1, analysis.getForwardTraceability().size());
        assertEquals("sA", analysis.getForwardTraceability().get(0).getSource().getId());
        assertEquals(1, analysis.getForwardTraceability().get(0).getTargets().size());
        assertEquals("tA", analysis.getForwardTraceability().get(0).getTargets().get(0).getId());
    }
}
