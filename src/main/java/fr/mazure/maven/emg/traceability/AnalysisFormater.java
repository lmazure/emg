package fr.mazure.maven.emg.traceability;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class AnalysisFormater {

    public void format(final OutputStream stream, final Analysis analysis) throws IOException {
        
        try (final BufferedOutputStream s = new BufferedOutputStream(stream);
             final PrintStream p = new PrintStream(s)) {
            p.println("<!DOCTYPE html>");            
            p.println("<html>");
            if (analysis.getErrors().size() > 0) {
                p.println("<h1>Errors</h1>");                            
                p.println("<table style='border-collapse: collapse;'>");
                for (String error: analysis.getErrors()) {
                    p.println("<tr><td style='border: 1px solid black;'>" + toHtml(error) + "</td></tr>");                                                
                }
                p.println("</table>");                            
            }
            if (analysis.getForwardTraceability().size() > 0) {
                p.println("<h1>Traceability</h1>");                            
                p.println("<table style='border-collapse: collapse;'>");
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
                for (ForwardTraceability trace: analysis.getForwardTraceability()) {
                    p.print("|" + trace.getSource().getId() + "|");
                    boolean first = true;
                    for (TargetElement elem: trace.getSortedTargets()) {
                        if (!first) {
                            p.print(", ");
                        }
                        p.print(elem.getId());
                        first = false;
                    }
                    p.println("|</br>");
                }
                p.println("</pre>");                                            
            }
            p.println("</html>");            
        }
    }
    
    static private String toHtml(final String str) {
        return str.replace("&", "amp;").replace(">", "gt;").replace("<", "lt;");
    }
}
