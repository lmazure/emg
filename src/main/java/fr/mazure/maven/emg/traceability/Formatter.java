package fr.mazure.maven.emg.traceability;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

abstract public class Formatter {

    /**
     * @param stream
     * @throws IOException
     */
    public void format(final OutputStream stream) throws IOException {
        
        try (final OutputStream s = new BufferedOutputStream(stream);
            final PrintStream p = new PrintStream(s, false, StandardCharsets.UTF_8.name())) {
            p.println("<!DOCTYPE html>");
            p.println("<html>");
            p.println("<head>");
            p.println("<meta http-equiv='Content-Type' content='text/html; charset=utf-8'>");
            p.println("</head>");
            p.println("<body>");
            writeContent(p);
            p.println("</body>");
            p.println("</html>");            
        }
    }
    
    abstract protected void writeContent(final PrintStream p);
    
    static protected String toHtml(final String str) {
        return str.replace("&", "amp;")
                  .replace(">", "gt;")
                  .replace("<", "lt;")
                  .replace("\n", "<br/>");
    }
}
