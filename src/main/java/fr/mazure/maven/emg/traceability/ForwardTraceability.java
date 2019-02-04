package fr.mazure.maven.emg.traceability;

import java.util.List;

public class ForwardTraceability {

    final private SourceElement _source;
    final private List<TargetElement> _targets;

    /**
     * @param source
     * @param targets
     */
    public ForwardTraceability(final SourceElement source, final List<TargetElement> targets) {
        _source = source;
        _targets = targets;
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
