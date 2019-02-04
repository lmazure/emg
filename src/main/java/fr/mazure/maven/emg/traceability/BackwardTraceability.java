package fr.mazure.maven.emg.traceability;

import java.util.List;

public class BackwardTraceability {

    final private TargetElement _target;
    final private List<String> _sourceIds;
    
    /**
     * @param target
     * @param sourceIds
     */
    public BackwardTraceability(final TargetElement target, final List<String> sourceIds) {
        _target = target;
        _sourceIds = sourceIds;
    }

    /**
     * @return the target
     */
    public TargetElement getTarget() {
        return _target;
    }

    /**
     * @return the sourceIds
     */
    public List<String> getSourceIds() {
        return _sourceIds;
    }
}
