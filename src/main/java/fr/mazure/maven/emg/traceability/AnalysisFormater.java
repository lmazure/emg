package fr.mazure.maven.emg.traceability;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AnalysisFormater {

    /**
     * output a traceability analysis:
     *  - traceability errors
     *  - traceability matrix
     *  - traceability matrix formatted in markup
     *  
     * @param stream
     * @param sourceName
     * @param targetName
     * @param sourceParsingErrors
     * @param targetParsingErrors
     * @param analysis
     * @throws IOException
     */
    public void format(final OutputStream stream,
                       final String sourceName,
                       final String targetName,
                       final List<String> sourceParsingErrors,
                       final List<String> targetParsingErrors,
                       final Analysis analysis) throws IOException {
        
        try (final OutputStream s = new BufferedOutputStream(stream);
            final PrintStream p = new PrintStream(s, false, StandardCharsets.UTF_8.name())) {
            p.println("<!DOCTYPE html>");
            p.println("<html>");
            p.println("<head>");
            p.println("<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>");
            p.println("</head>");
            p.println("<body>");
            if (sourceParsingErrors.size() > 0) {
                p.println("<h1>Specification parsing errors</h1>");                            
                p.println("<table style='border-collapse: collapse;'>");
                for (String error: sourceParsingErrors) {
                    p.println("<tr><td style='border: 1px solid black;'>" + toHtml(error) + "</td></tr>");                                                
                }
                p.println("</table>");                            
            }
            if (targetParsingErrors.size() > 0) {
                p.println("<h1>Test parsing errors</h1>");                            
                p.println("<table style='border-collapse: collapse;'>");
                for (String error: targetParsingErrors) {
                    p.println("<tr><td style='border: 1px solid black;'>" + toHtml(error) + "</td></tr>");                                                
                }
                p.println("</table>");                            
            }
            if (analysis.getErrors().size() > 0) {
                p.println("<h1>Traceability errors</h1>");                            
                p.println("<table style='border-collapse: collapse;'>");
                for (String error: analysis.getErrors()) {
                    p.println("<tr><td style='border: 1px solid black;'>" + toHtml(error) + "</td></tr>");                                                
                }
                p.println("</table>");                            
            }
            if (analysis.getForwardTraceability().size() > 0) {
                p.println("<h1>Traceability</h1>");                            
                p.println("<table style='border-collapse: collapse;'>");
                p.println("<tr><th style='border: 1px solid black;'>" + toHtml(sourceName) + "</th><th style='border: 1px solid black;'>" + toHtml(targetName) + "</th></tr>");
                for (ForwardTraceability trace: analysis.getForwardTraceability()) {
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
                p.println("h|*" + toHtml(sourceName) + "*|*" + toHtml(targetName) + "*|");                                            
                for (ForwardTraceability trace: analysis.getForwardTraceability()) {
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
                p.println("<h1>Test report as markup</h1>");                            
                p.println("<pre>");                                            
                p.println("h|*" + toHtml(sourceName) + "*|*itération 1*|*itération 2*|*itération 3*|bug ID|commentaire|");                                            
                for (ForwardTraceability trace: analysis.getForwardTraceability()) {
                    p.println("|" + toHtml(trace.getSource().getId()) + "||||||");
                }
                p.println("</pre>");                                            
            }
            p.println("</body>");
            p.println("</html>");            
        }
    }
    
    static private String toHtml(final String str) {
        return str.replace("&", "amp;")
                  .replace(">", "gt;")
                  .replace("<", "lt;")
                  .replace("\n", "<br/>");
    }
}
