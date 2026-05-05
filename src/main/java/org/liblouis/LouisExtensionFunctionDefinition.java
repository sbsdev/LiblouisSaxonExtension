package org.liblouis;

import java.util.HashMap;
import java.util.Map;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.lib.ExtensionFunctionCall;
import net.sf.saxon.lib.ExtensionFunctionDefinition;
import net.sf.saxon.om.Item;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.om.StructuredQName;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.EmptySequence;
import net.sf.saxon.value.SequenceType;
import net.sf.saxon.value.StringValue;

/**
 * Copyright (C) 2010 Swiss Library for the Blind, Visually Impaired and Print Disabled
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

public class LouisExtensionFunctionDefinition extends ExtensionFunctionDefinition {

    private static final StructuredQName FUNC_NAME = new StructuredQName(
            "louis", "http://liblouis.org/liblouis", "translate");

    @Override
    public StructuredQName getFunctionQName() {
        return FUNC_NAME;
    }

    @Override
    public int getMinimumNumberOfArguments() {
        return 2;
    }

    @Override
    public int getMaximumNumberOfArguments() {
        return 2;
    }

    @Override
    public SequenceType[] getArgumentTypes() {
        return new SequenceType[]{ SequenceType.SINGLE_STRING, SequenceType.SINGLE_STRING };
    }

    @Override
    public SequenceType getResultType(SequenceType[] suppliedArgumentTypes) {
        return SequenceType.SINGLE_STRING;
    }

    @Override
    public ExtensionFunctionCall makeCallExpression() {
        return new ExtensionFunctionCall() {
            private final Map<String, Translator> cache = new HashMap<>();

            @Override
            public Sequence call(XPathContext context, Sequence[] arguments) throws XPathException {
                Item tableItem = arguments[0].head();
                Item textItem = arguments[1].head();
                if (tableItem == null || textItem == null) {
                    return EmptySequence.getInstance();
                }

                String table = tableItem.getStringValue();
                String text = textItem.getStringValue();

                Translator translator = cache.get(table);
                if (translator == null) {
                    try {
                        translator = new Translator(table);
                        cache.put(table, translator);
                    } catch (CompilationException e) {
                        throw new XPathException("Failed to compile liblouis table '" + table + "': " + e.getMessage());
                    }
                }

                try {
                    return new StringValue(translator.translate(text, null, null, null).getBraille());
                } catch (TranslationException | DisplayException e) {
                    throw new XPathException("liblouis translation failed: " + e.getMessage());
                }
            }
        };
    }
}
