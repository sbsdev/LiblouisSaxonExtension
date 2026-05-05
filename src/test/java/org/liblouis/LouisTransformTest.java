/*
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

class SysOutSaver {
    @FunctionalInterface
    interface Hook {
        void hook();
    }

    static String process(Hook hook) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream saved = System.out;
        System.setOut(new PrintStream(baos));
        hook.hook();
        System.setOut(saved);
        return baos.toString();
    }
}

class LouisTransformTest {

    @Test
    void testTransform() {
        String sysout = SysOutSaver.process(() -> {
            String[] args = { "-xsl:resources/test.xsl", "-s:resources/test.xml" };
            new LouisTransform().doTransform(args, "java org.liblouis.LouisTransform");
        });
        assertEquals(",! qk br{n fox jumps ov} ! lazy dog4", sysout.trim());
    }
}
