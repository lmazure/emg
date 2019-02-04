package fr.mazure.maven.emg.traceability;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class Analysis {

    final private SortedSet<ForwardTraceability> _analysis;
    final private List<String> _errors;
    
    public Analysis() {
        _analysis = new TreeSet<ForwardTraceability>((a, b) -> a.getSource().getId().compareTo(b.getSource().getId()));
        _errors = new LinkedList<String>();        
    }
    
    public void addForwardTraceability(final ForwardTraceability traceability) {
        _analysis.add(traceability);
    }
    
    public void addError(final String error) {
        _errors.add(error);
    }
    
    public List<ForwardTraceability> getForwardTraceability() {
        return new ArrayList<ForwardTraceability>(_analysis);
    }
    
    public List<String> getErrors() {
        return _errors;
    }
}
