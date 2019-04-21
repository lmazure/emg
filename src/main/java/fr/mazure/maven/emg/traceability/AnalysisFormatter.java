package fr.mazure.maven.emg.traceability;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

public class AnalysisFormatter extends Formatter {

    final private String _sourceName;
    final private String _targetName;
    private List<String> _sourceParsingErrors;
    private List<String> _targetParsingErrors;
    private Analysis _analysis;

    /**
     * @param _sourceName
     * @param _targetName
     */
    public AnalysisFormatter(final String sourceName,
                             final String targetName) {
        _sourceName = sourceName;
        _targetName = targetName;
    }
    
    /**
     * output a traceability analysis:
     *  - traceability errors
     *  - traceability matrix
     *  - traceability matrix formatted in markup
     *  
     * @param stream
     * @param sourceParsingErrors
     * @param targetParsingErrors
     * @param analysis
     * @throws IOException
     */
    public void format(final OutputStream stream,
                       final List<String> sourceParsingErrors,
                       final List<String> targetParsingErrors,
                       final Analysis analysis) throws IOException {
        
        _sourceParsingErrors = sourceParsingErrors;
        _targetParsingErrors = targetParsingErrors;
        _analysis = analysis;
        
        super.format(stream);
        
    }
    
    @Override
    protected void writeContent(final PrintStream p) {

        if (_sourceParsingErrors.size() > 0) {
            p.println("<h1>Specification parsing errors</h1>");                            
            p.println("<table style='border-collapse: collapse;'>");
            for (String error: _sourceParsingErrors) {
                p.println("<tr><td style='border: 1px solid black;'>" + toHtml(error) + "</td></tr>");                                                
            }
            p.println("</table>");                            
        }
        if (_targetParsingErrors.size() > 0) {
            p.println("<h1>Test parsing errors</h1>");                            
            p.println("<table style='border-collapse: collapse;'>");
            for (String error: _targetParsingErrors) {
                p.println("<tr><td style='border: 1px solid black;'>" + toHtml(error) + "</td></tr>");                                                
            }
            p.println("</table>");                            
        }
        if (_analysis.getErrors().size() > 0) {
            p.println("<h1>Traceability errors</h1>");                            
            p.println("<table style='border-collapse: collapse;'>");
            for (String error: _analysis.getErrors()) {
                p.println("<tr><td style='border: 1px solid black;'>" + toHtml(error) + "</td></tr>");                                                
            }
            p.println("</table>");                            
        }
        if (_analysis.getForwardTraceability().size() > 0) {
            p.println("<h1>Traceability</h1>");                            
            p.println("<table style='border-collapse: collapse;'>");
            p.println("<tr><th style='border: 1px solid black;'>" + toHtml(_sourceName) + "</th><th style='border: 1px solid black;'>" + toHtml(_targetName) + "</th></tr>");
            for (ForwardTraceability trace: _analysis.getForwardTraceability()) {
                p.print("<tr><td style='border: 1px solid black;'>" + toHtml(trace.getSource().getId()) + "</td><td style='border: 1px solid black;'>");
                boolean first = true;
                for (TargetElement elem: trace.getSortedTargets()) {
                    if (!first) {
                        p.print(", ");
                    }
                    p.print(elem.getId());
                    first = false;
                }
                p.println("</td></tr>");                                                
            }
            p.println("</table>");                  
            p.println("<h1>Traceability as markup</h1>");                            
            p.println("<pre>");                                            
            p.println("h|*" + toHtml(_sourceName) + "*|*" + toHtml(_targetName) + "*|");                                            
            for (ForwardTraceability trace: _analysis.getForwardTraceability()) {
                p.print("|" + toHtml(trace.getSource().getId()) + "|");
                boolean first = true;
                for (TargetElement elem: trace.getSortedTargets()) {
                    if (!first) {
                        p.print(", ");
                    }
                    p.print(toHtml(elem.getId()));
                    first = false;
                }
                p.println("|");
            }
            p.println("</pre>");                                            
        }
    }
}
