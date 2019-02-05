package fr.mazure.maven.emg.traceability;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class Analysis {

    final private SortedSet<ForwardTraceability> _analysis;
    final private SortedSet<String> _errors;
    
    /**
     * 
     */
    public Analysis() {
        _analysis = new TreeSet<ForwardTraceability>((a, b) -> a.getSource().getId().compareTo(b.getSource().getId()));
        _errors = new TreeSet<String>();        
    }
    
    /**
     * @param traceability
     */
    public void addForwardTraceability(final ForwardTraceability traceability) {
        _analysis.add(traceability);
    }
    
    /**
     * @param error
     */
    public void addError(final String error) {
        _errors.add(error);
    }
    
    /**
     * @return sorted list of forward traceabilities
     */
    public List<ForwardTraceability> getForwardTraceability() {
        return new ArrayList<ForwardTraceability>(_analysis);
    }
    
    /**
     * @return sorted list of errors
     */
    public List<String> getErrors() {
        return new ArrayList<String>(_errors);
    }
}
