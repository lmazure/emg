package fr.mazure.maven.emg.traceability;

import java.util.List;

public class ForwardTraceability {

    final private CommentedSourceElement _source;
    final private List<CommentedTargetElement> _targets;

    /**
     * @param source
     * @param targets
     */
    public ForwardTraceability(final CommentedSourceElement source, final List<CommentedTargetElement> targets) {
        _source = source;
        _targets = targets;
    }

    /**
     * @return the source
     */
    public CommentedSourceElement getSource() {
        return _source;
    }

    /**
     * @return the targets
     */
    public List<CommentedTargetElement> getTargets() {
        return _targets;
    }
}
