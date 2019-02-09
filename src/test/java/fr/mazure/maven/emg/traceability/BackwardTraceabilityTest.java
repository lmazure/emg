package fr.mazure.maven.emg.traceability;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class BackwardTraceabilityTest {

    @Test
    void test() {
        final List<String> sourceIds = Arrays.asList("D", "B", "A", "C");
        final TargetElement target = new TargetElement("myId", "myLocation");
        final BackwardTraceability trace = new BackwardTraceability(target, sourceIds);

        assertEquals("myId", trace.getTarget().getId());
        assertEquals(4, trace.getSortedSourceIds().size());
        assertEquals("A", trace.getSortedSourceIds().get(0));
        assertEquals("B", trace.getSortedSourceIds().get(1));
        assertEquals("C", trace.getSortedSourceIds().get(2));
        assertEquals("D", trace.getSortedSourceIds().get(3));
        
        // check that the initial list has not been modified
        assertEquals(4, sourceIds.size());
        assertEquals("D", sourceIds.get(0));
        assertEquals("B", sourceIds.get(1));
        assertEquals("A", sourceIds.get(2));
        assertEquals("C", sourceIds.get(3));
    }

}
