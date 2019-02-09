package fr.mazure.maven.emg.traceability;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ForwardTraceabilityTest {

    @Test
    void test() {
        
        final SourceElement source = new SourceElement("myId", "myLocation");
        final List<TargetElement> targets = new ArrayList<TargetElement>();
        targets.add(new TargetElement("target D", "location 1"));
        targets.add(new TargetElement("target B", "location 2"));
        targets.add(new TargetElement("target A", "location 3"));
        targets.add(new TargetElement("target C", "location 4"));
        
        final ForwardTraceability trace = new ForwardTraceability(source, targets);

        assertEquals("myId", trace.getSource().getId());
        assertEquals(4, trace.getSortedTargets().size());
        assertEquals("target A", trace.getSortedTargets().get(0).getId());
        assertEquals("target B", trace.getSortedTargets().get(1).getId());
        assertEquals("target C", trace.getSortedTargets().get(2).getId());
        assertEquals("target D", trace.getSortedTargets().get(3).getId());
        
        // check that the initial list has not been modified
        assertEquals(4, targets.size());
        assertEquals("target D", targets.get(0).getId());
        assertEquals("target B", targets.get(1).getId());
        assertEquals("target A", targets.get(2).getId());
        assertEquals("target C", targets.get(3).getId());
    }

}
