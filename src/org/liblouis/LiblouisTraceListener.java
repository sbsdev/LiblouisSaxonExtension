package org.liblouis;

import java.util.Iterator;

import net.sf.saxon.expr.Expression;
import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.functions.IntegratedFunctionCall;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.StandardNames;
import net.sf.saxon.trace.InstructionInfo;
import net.sf.saxon.trace.Location;
import net.sf.saxon.trace.TraceListener;

public class LiblouisTraceListener implements TraceListener {
	
	public void open() {}
	public void close() {}
	
	public void enter(InstructionInfo instruction, XPathContext context) {
		
		int type = instruction.getConstructType();
		switch (type) {
			case StandardNames.XSL_TEMPLATE:
				Object matcher = instruction.getProperty("match");
				if (matcher != null && ((String)matcher).startsWith("text()"))
					System.err.println("<template match=\"" + matcher + "\"/>");
				break;
			case Location.FUNCTION_CALL:
				String name = instruction.getObjectName().getDisplayName();
				if (name.equals("louis:translate")) {
					try {
						IntegratedFunctionCall call = (IntegratedFunctionCall)instruction.getProperty("expression");
						Iterator<Expression> arguments = call.iterateSubExpressions();
						String table = arguments.next().evaluateAsString(context).toString();
						String text = arguments.next().evaluateAsString(context).toString();
						System.err.println("louis:translate(\n\t'" + table + "',\n\t'" + text + "')"); }
					catch (Exception e) {}}
				break;
			default:
				break; }
	}
	
	public void leave(InstructionInfo instruction) {}
	public void startCurrentItem(Item item) {}
	public void endCurrentItem(Item item) {}
}
