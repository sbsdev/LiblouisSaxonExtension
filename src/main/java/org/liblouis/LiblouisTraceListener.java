/**
 * Copyright (C) 2010-2026 Swiss Library for the Blind, Visually Impaired and Print Disabled
 *
 * This file is part of LiblouisSaxonExtension.
 *
 * LiblouisSaxonExtension is free software: you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

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
