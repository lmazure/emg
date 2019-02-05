package fr.mazure.maven.emg.traceability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class TraceabilityAnalyzer {

    public TraceabilityAnalyzer() {
    }

    public Analysis analyze(final List<SourceElement> sources, final List<BackwardTraceability> targetTraceabilities) {
        
        
        /*final SortedSet<CommentedSourceElement> commentedSources = new TreeSet<CommentedSourceElement>(); 
        
        for (SourceElement source: sources) { // TODO tester la boucle avec 2, 3, et 4 duplications
            
            String comment = "";
            for (CommentedSourceElement s : commentedSources) {
                if ( s.getSource().getId().equals(source.getId())) {
                    if ( s.getComment().isEmpty() ) {
                        comment = "Id " + s.getSource().getId() + " is duplicated\n"
                                  + "- " + s.getSource().getLocation() + "\n";
                    }
                    comment += "- " + source.getLocation();
                }
                commentedSources.remove(s); // TODO je pense que cela ne marche pas de retirer l'élement dans la boucle
            }
            
            commentedSources.add(new CommentedSourceElement(source, comment));
        }
     
        List<ForwardTraceability> list = new ArrayList<ForwardTraceability>();*/
        
        final Analysis analysis = new Analysis();
        
        // detect duplicated source IDs
        detectDuplicatedIds(sources, "source Id", analysis);

        // detect duplicated target IDs
        final List<TargetElement> list = new ArrayList<TargetElement>();
        for (BackwardTraceability bt: targetTraceabilities) list.add(bt.getTarget());
        detectDuplicatedIds(list, "target Id", analysis);
        

        return analysis;
    }

    private void detectDuplicatedIds(final List<? extends Element> elements, final String elementDescription, final Analysis analysis) {
        
        final Map<String, SortedSet<String>> locations = new HashMap<String, SortedSet<String>>();
        for (Element source: elements) {
            SortedSet<String> l = locations.get(source.getId());
            if (l == null) {
                l = new TreeSet<String>();
                locations.put(source.getId(), l);
            }
            l.add(source.getLocation());
        }
        for (String id: locations.keySet()) {
            SortedSet<String> l = locations.get(id);
            if (l.size() > 1) {
                analysis.addError(elementDescription + " '" + id + "' is duplicated:\n- " + String.join("\n- ", l));
            }
        }
    }
}
