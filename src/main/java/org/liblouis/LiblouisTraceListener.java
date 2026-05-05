package org.liblouis;

import java.util.Map;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.functions.IntegratedFunctionCall;
import net.sf.saxon.lib.TraceListener;
import net.sf.saxon.om.Item;
import net.sf.saxon.trace.Traceable;
import net.sf.saxon.trace.TraceableComponent;

public class LiblouisTraceListener implements TraceListener {

    @Override
    public void enter(Traceable instruction, Map<String, Object> properties, XPathContext context) {
        int line = instruction.getLocation().getLineNumber();
        if (instruction instanceof TraceableComponent
                && "xsl:template".equals(((TraceableComponent) instruction).getTracingTag())) {
            Object matcher = properties.get("match");
            if (matcher != null && matcher.toString().startsWith("text()"))
                System.err.println(line + ": <template match=\"" + matcher + "\"/>");
        } else if (instruction instanceof IntegratedFunctionCall) {
            IntegratedFunctionCall call = (IntegratedFunctionCall) instruction;
            if ("louis:translate".equals(call.getFunctionName().getDisplayName())) {
                try {
                    String table = call.getArg(0).evaluateAsString(context).toString();
                    String text = call.getArg(1).evaluateAsString(context).toString();
                    System.err.println(line + ": louis:translate(\n\t'" + table + "',\n\t'" + text + "')");
                } catch (Exception e) {}
            }
        }
    }

    @Override
    public void leave(Traceable instruction) {}

    @Override
    public void startCurrentItem(Item item) {}

    @Override
    public void endCurrentItem(Item item) {}
}
