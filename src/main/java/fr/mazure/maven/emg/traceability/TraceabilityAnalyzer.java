package fr.mazure.maven.emg.traceability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class TraceabilityAnalyzer {

    public TraceabilityAnalyzer() {
    }

    public Analysis analyze(final List<SourceElement> sources, final List<BackwardTraceability> targetTraceabilities) {
        
        final Analysis analysis = new Analysis();
        
        // detect duplicated source IDs
        detectDuplicatedElements(sources, "source Id", analysis);

        // detect duplicated target IDs
        final List<TargetElement> list = new ArrayList<TargetElement>();
        for (BackwardTraceability bt: targetTraceabilities) list.add(bt.getTarget());
        detectDuplicatedElements(list, "target Id", analysis);

        // keep a record of the real source Ids
        final Set<String> realSourceIds = new HashSet<String>();
        for (SourceElement s: sources) realSourceIds.add(s.getId());
        
        // create indexed map of source elements (we will add pseudo source elements later on)
        final Map<String, SourceElement> indexedMapOfSourceElement = new HashMap<String, SourceElement>();
        for (SourceElement source: sources) {
            if (!indexedMapOfSourceElement.containsKey(source.getId()) ) {
                indexedMapOfSourceElement.put(source.getId(), source);
            }
        }

        // build the forward traceability
        final Map<SourceElement, List<TargetElement>> forwardTraceabilities = new HashMap<SourceElement, List<TargetElement>>();
        for (SourceElement s: sources) forwardTraceabilities.put(s, new ArrayList<TargetElement>());
        
        for (BackwardTraceability bt: targetTraceabilities) {

            if (bt.getSourceIds().size() == 0) {
                analysis.addError("target Id '" + bt.getTarget().getId() + "' has no backward traceability");
                break;
            }
            
            detectDuplicatedIds(bt.getSourceIds(), "the backward traceability of target Id '" + bt.getTarget().getId() + "'", analysis);
            
            for (String sourceId: bt.getSourceIds()) {
                String id;
                if (!realSourceIds.contains(sourceId)) {
                    analysis.addError("target Id '" + bt.getTarget().getId() + "' refers a non-existing source Id: '" + sourceId + "'");
                    id = "‽ " + sourceId + " ‽";
                    if (!indexedMapOfSourceElement.containsKey(id)) {
                        final SourceElement pseudoSourceElement = new SourceElement(id, ""); 
                        indexedMapOfSourceElement.put(id, pseudoSourceElement);
                        forwardTraceabilities.put(pseudoSourceElement, new ArrayList<TargetElement>());
                    }
                } else {
                    id = sourceId;
                }
                forwardTraceabilities.get(indexedMapOfSourceElement.get(id)).add(bt.getTarget());
            }
        }

        for (SourceElement s: forwardTraceabilities.keySet()) analysis.addForwardTraceability(new ForwardTraceability(s, forwardTraceabilities.get(s)));
        
        return analysis;
    }

    static private void detectDuplicatedElements(final List<? extends Element> elements, final String elementDescription, final Analysis analysis) {
        
        final Map<String, SortedSet<String>> idToLocationMap = new HashMap<String, SortedSet<String>>();
        for (Element element: elements) {
            SortedSet<String> l = idToLocationMap.get(element.getId());
            if (l == null) {
                l = new TreeSet<String>();
                idToLocationMap.put(element.getId(), l);
            }
            l.add(element.getLocation());
        }

        for (String id: idToLocationMap.keySet()) {
            SortedSet<String> l = idToLocationMap.get(id);
            if (l.size() > 1) {
                analysis.addError(elementDescription + " '" + id + "' is duplicated:\n- " + String.join("\n- ", l));
            }
        }
    }
    
    static private void detectDuplicatedIds(List<String> ids, final String listDescription, final Analysis analysis) {

        final Map<String, Integer> idToCountMap = new HashMap<String, Integer>();
        for (String id: ids) {
            Integer l = idToCountMap.get(id);
            if (l == null) {
                l = Integer.valueOf(1);;
                idToCountMap.put(id, l);
            } else {
                idToCountMap.put(id, l + 1);
            }
        }

        for (String id: idToCountMap.keySet()) {
            if (idToCountMap.get(id) > 1) {
                analysis.addError(listDescription + " contains a duplicated source Id: '" + id + "'");
            }
        }
        
    }
}
