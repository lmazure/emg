package fr.mazure.maven.emg.traceability;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
        
        return analysis;
    }
}
