package fr.mazure.maven.emg.traceability;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

public class TargetListReportFormatter extends Formatter {

    final private String _targetName;
    final private String[] _annotations;
    private List<BackwardTraceability> _targetTraceabilities;

    /**
     * output an annotated report of the targets
     * 
     * @param targetName
     * @param annotations
     */
    public TargetListReportFormatter(final String targetName,
                                     final String[] annotations) {
        _targetName = targetName;
        _annotations = annotations;
    }
    
    public void format(final OutputStream stream,
                       final List<BackwardTraceability> targetTraceabilities) throws IOException {

        _targetTraceabilities = targetTraceabilities;

        super.format(stream);
    }

    @Override
    protected void writeContent(final PrintStream p) {
        
        p.println("<h1>" + _targetName + " report as markup</h1>");                            
        p.println("<pre>");                                            
        p.print("h|*" + toHtml(_targetName) + "*|");
        for (String annot: _annotations) {
            p.print("*" + toHtml(annot) + "*|");            
        }
        p.println();
        for (BackwardTraceability trace: _targetTraceabilities) {
            p.print("|" + toHtml(trace.getTarget().getId()) + "|");
            for (int i = 0; i < _annotations.length; i++) {
                p.print("|");            
            }
            p.println();
        }
        p.println("</pre>");                                                    
    }
}
