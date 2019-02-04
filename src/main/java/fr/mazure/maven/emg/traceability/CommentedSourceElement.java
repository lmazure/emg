package fr.mazure.maven.emg.traceability;

public class CommentedSourceElement {

    final private SourceElement _source;
    final private String _comment;

    /**
     * @param source
     * @param comment
     */
    public CommentedSourceElement(final SourceElement source, final String comment) {
        _source = source;
        _comment = comment;
    }

    /**
     * @return the source
     */
    public SourceElement getSource() {
        return _source;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return _comment;
    }
}
