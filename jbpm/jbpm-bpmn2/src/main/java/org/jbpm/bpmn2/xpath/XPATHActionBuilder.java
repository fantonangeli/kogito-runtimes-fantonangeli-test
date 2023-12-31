/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jbpm.bpmn2.xpath;

import java.util.HashMap;
import java.util.Map;

import org.drools.compiler.compiler.DescrBuildError;
import org.drools.compiler.rule.builder.PackageBuildContext;
import org.drools.drl.ast.descr.ActionDescr;
import org.jbpm.process.builder.ActionBuilder;
import org.jbpm.process.core.ContextResolver;
import org.jbpm.workflow.core.DroolsAction;
import org.mvel2.Macro;
import org.mvel2.MacroProcessor;

public class XPATHActionBuilder
        implements
        ActionBuilder {

    private static final Map macros = new HashMap(5);
    static {
        macros.put("insert",
                new Macro() {
                    public String doMacro() {
                        return "kcontext.getKieRuntime().insert";
                    }
                });
    }

    public XPATHActionBuilder() {

    }

    public void build(final PackageBuildContext context,
            final DroolsAction action,
            final ActionDescr actionDescr,
            final ContextResolver contextResolver) {

        try {
        } catch (final Exception e) {
            context.getErrors().add(new DescrBuildError(context.getParentDescr(),
                    actionDescr,
                    null,
                    "Unable to build expression for action '" + actionDescr.getText() + "' :" + e));
        }
    }

    public static String processMacros(String consequence) {
        MacroProcessor macroProcessor = new MacroProcessor();
        macroProcessor.setMacros(macros);
        return macroProcessor.parse(delimitExpressions(consequence));
    }

    /**
     * Allows newlines to demarcate expressions, as per MVEL command line.
     * If expression spans multiple lines (ie inside an unbalanced bracket) then
     * it is left alone.
     * Uses character based iteration which is at least an order of magnitude faster then a single
     * simple regex.
     */
    public static String delimitExpressions(String s) {

        StringBuilder result = new StringBuilder();
        char[] cs = s.toCharArray();
        int brace = 0;
        int sqre = 0;
        int crly = 0;
        char lastNonWhite = ';';
        for (int i = 0; i < cs.length; i++) {
            char c = cs[i];
            switch (c) {
                case '(':
                    brace++;
                    break;
                case '{':
                    crly++;
                    break;
                case '[':
                    sqre++;
                    break;
                case ')':
                    brace--;
                    break;
                case '}':
                    crly--;
                    break;
                case ']':
                    sqre--;
                    break;
                default:
                    break;
            }
            if ((brace == 0 && sqre == 0 && crly == 0) && (c == '\n' || c == '\r')) {
                if (lastNonWhite != ';') {
                    result.append(';');
                    lastNonWhite = ';';
                }
            } else if (!Character.isWhitespace(c)) {
                lastNonWhite = c;
            }
            result.append(c);

        }
        return result.toString();
    }

}
