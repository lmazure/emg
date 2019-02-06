package fr.mazure.maven.emg.traceability;

import java.util.Collections;
import java.util.List;

public class ForwardTraceability {

    final private SourceElement _source;
    final private List<TargetElement> _targets;

    /**
     * @param source
     * @param targets (may not be sorted)
     */
    public ForwardTraceability(final SourceElement source, final List<TargetElement> targets) {
        _source = source;
        _targets = targets;
        Collections.sort(_targets, (a, b) -> a.getId().compareTo(b.getId()));
    }

    /**
     * @return the source
     */
    public SourceElement getSource() {
        return _source;
    }

    /**
     * @return the targets
     */
    public List<TargetElement> getTargets() {
        return _targets;
    }
}
