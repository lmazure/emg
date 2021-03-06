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

    final private String _sourceName;
    final private String _targetName;

    /**
     * create a traceability analyzer
     * 
     * @param sourceName name of the type of the sources of the traceability (this is only used to generate messages that are easier to understand) 
     * @param targetName name of the type of the targets of the traceability (this is only used to generate messages that are easier to understand)
     */
    public TraceabilityAnalyzer(final String sourceName,
                                final String targetName) {
        
        _sourceName = sourceName;
        _targetName = targetName;
    }

    /**
     * generate an analysis
     * 
     * @param sources
     * @param targetTraceabilities
     * @return
     */
    public Analysis analyze(final List<SourceElement> sources,
                            final List<BackwardTraceability> targetTraceabilities) {
        
        final Analysis analysis = new Analysis();
        
        // detect duplicated or empty source IDs
        detectDuplicatedOrEmptyElements(sources, _sourceName + " Id", analysis);

        // detect duplicated or empty target IDs
        final List<TargetElement> list = new ArrayList<TargetElement>();
        for (BackwardTraceability bt: targetTraceabilities) list.add(bt.getTarget());
        detectDuplicatedOrEmptyElements(list, _targetName + " Id", analysis);

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

            if (bt.getSortedSourceIds().size() == 0) {
                analysis.addError(_targetName + " Id '" + bt.getTarget().getId() + "' has no backward traceability");
                break;
            }
            
            detectDuplicatedIds(bt.getSortedSourceIds(), "the backward traceability of " + _targetName + " Id '" + bt.getTarget().getId() + "'", analysis);
            
            for (String sourceId: bt.getSortedSourceIds()) {
                String id;
                if (!realSourceIds.contains(sourceId)) {
                    analysis.addError(_targetName + " Id '" + bt.getTarget().getId() + "' refers a non-existing " + _sourceName + " Id: '" + sourceId + "'");
                    id = generateFakeSourceId(sourceId);
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

        for (SourceElement s: forwardTraceabilities.keySet()) {
            analysis.addForwardTraceability(new ForwardTraceability(s, forwardTraceabilities.get(s)));
        }
        
        return analysis;
    }

    static private void detectDuplicatedOrEmptyElements(final List<? extends Element> elements, final String elementDescription, final Analysis analysis) {
        
        final Map<String, SortedSet<String>> idToLocationMap = new HashMap<String, SortedSet<String>>();
        for (Element element: elements) {
            if (element.getId().isEmpty()) {
                analysis.addError("empty " + elementDescription + " at " + element.getLocation());                
            } else {
                SortedSet<String> l = idToLocationMap.get(element.getId());
                if (l == null) {
                    l = new TreeSet<String>();
                    idToLocationMap.put(element.getId(), l);
                }
                l.add(element.getLocation());                
            }
        }

        for (String id: idToLocationMap.keySet()) {
            final SortedSet<String> l = idToLocationMap.get(id);
            if (l.size() > 1) {
                analysis.addError(elementDescription + " '" + id + "' is duplicated:\n- " + String.join("\n- ", l));
            }
        }
    }
    
    private void detectDuplicatedIds(final List<String> ids,
                                     final String listDescription,
                                     final Analysis analysis) {

        final Set<String> allIds = new HashSet<String>();
        final Set<String> duplicateIds = new HashSet<String>();
        for (String id: ids) {
            (allIds.contains(id) ? duplicateIds : allIds).add(id);
        }

        for (String id: duplicateIds) {
            analysis.addError(listDescription + " contains a duplicated " + _sourceName + " Id: '" + id + "'");
        }
    }
    
    /**
     * fake Id used when a target Id refers a non-existing source Id
     * 
     * @param sourceId
     * @return
     */
    private String generateFakeSourceId(final String sourceId) {
        return "‽ non-existing " + _sourceName + " Id: " + sourceId + " ‽"; 
    }
}
