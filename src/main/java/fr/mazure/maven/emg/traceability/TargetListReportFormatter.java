package fr.mazure.maven.emg.traceability;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

public class TargetListReportFormatter extends Formatter {

    final private String _targetName;
    final private String _iterationName;
    final private String _bugName;
    final private String _commentName;
    private List<BackwardTraceability> _targetTraceabilities;

    /**
     * output an annotated list of the targets
     * @param _targetName
     * @param _iterationName
     * @param _bugName
     * @param _commentName
     */
    public TargetListReportFormatter(final String targetName,
                               final String iterationName,
                               final String bugName,
                               final String commentName) {
        _targetName = targetName;
        _iterationName = iterationName;
        _bugName = bugName;
        _commentName = commentName;
    }
    
    public void format(final OutputStream stream,
            final List<BackwardTraceability> targetTraceabilities) throws IOException {

        _targetTraceabilities = targetTraceabilities;

        super.format(stream);

    }

    @Override
    protected void writeContent(final PrintStream p) {
        
        p.println("<h1>Test report as markup</h1>");                            
        p.println("<pre>");                                            
        p.println("h|*"
                  + toHtml(_targetName) + "*|*"
                  + _iterationName + " 1*|*"
                  + _iterationName + " 2*|*" 
                  + _iterationName + " 3*|"
                  + _bugName + "|"
                  + _commentName + "|");                                            
        for (BackwardTraceability trace: _targetTraceabilities) {
            p.println("|" + toHtml(trace.getTarget().getId()) + "||||||");
        }
        p.println("</pre>");                                                    
    }
}
