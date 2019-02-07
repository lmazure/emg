package fr.mazure.maven.emg.traceability;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BackwardTraceability {

    final private TargetElement _target;
    final private List<String> _sourceIds;
    
    /**
     * @param target
     * @param sourceIds (may not be sorted)
     */
    public BackwardTraceability(final TargetElement target, final List<String> sourceIds) {
        _target = target;
        _sourceIds = new ArrayList<String>(sourceIds);
        Collections.sort(_sourceIds);
    }

    /**
     * @return the target
     */
    public TargetElement getTarget() {
        return _target;
    }

    /**
     * @return the sorted sourceIds
     */
    public List<String> getSourceIds() {
        return _sourceIds;
    }
}
