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
package org.jbpm.workflow.instance.node;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.jbpm.test.util.AbstractBaseTest;
import org.jbpm.util.PatternConstants;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class ParameterResolverTest extends AbstractBaseTest {

    public void addLogger() {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @Test
    public void testSingleVariable() {

        String[] expected = new String[] { "var1" };
        String s = "#{var1}";

        List<String> foundVariables = new ArrayList<String>();

        Matcher matcher = PatternConstants.PARAMETER_MATCHER.matcher(s);
        while (matcher.find()) {
            String paramName = matcher.group(1);

            foundVariables.add(paramName);
        }

        assertThat(foundVariables).hasSize(1).containsExactly(expected);
    }

    @Test
    public void testSingleVariableEnclosedWithText() {

        String[] expected = new String[] { "var1" };
        String s = "this is my #{var1} variable";

        List<String> foundVariables = new ArrayList<String>();

        Matcher matcher = PatternConstants.PARAMETER_MATCHER.matcher(s);
        while (matcher.find()) {
            String paramName = matcher.group(1);

            foundVariables.add(paramName);
        }

        assertThat(foundVariables).hasSize(1).containsExactly(expected);
    }

    @Test
    public void testMultiVariableWithoutWhitespace() {

        String[] expected = new String[] { "var1", "var2" };
        String s = "#{var1}=#{var2}";

        List<String> foundVariables = new ArrayList<String>();

        Matcher matcher = PatternConstants.PARAMETER_MATCHER.matcher(s);
        while (matcher.find()) {
            String paramName = matcher.group(1);

            foundVariables.add(paramName);
        }

        assertThat(foundVariables).hasSize(2).containsExactly(expected);
    }

    @Test
    public void testMultiVariableSeparatedWithComma() {

        String[] expected = new String[] { "var1", "var2" };
        String s = "#{var1},#{var2}";

        List<String> foundVariables = new ArrayList<String>();

        Matcher matcher = PatternConstants.PARAMETER_MATCHER.matcher(s);
        while (matcher.find()) {
            String paramName = matcher.group(1);

            foundVariables.add(paramName);
        }

        assertThat(foundVariables).hasSize(2).containsExactly(expected);
    }

    @Test
    public void testMultiVariableEnclosedWithText() {

        String[] expected = new String[] { "var1", "var2" };
        String s = "Here are my two #{var1},#{var2} variables";

        List<String> foundVariables = new ArrayList<String>();

        Matcher matcher = PatternConstants.PARAMETER_MATCHER.matcher(s);
        while (matcher.find()) {
            String paramName = matcher.group(1);

            foundVariables.add(paramName);
        }

        assertThat(foundVariables).hasSize(2).containsExactly(expected);
    }

    @Test
    public void testMultiVariableNextToEachOther() {

        String[] expected = new String[] { "var1", "var2" };
        String s = "#{var1}#{var2}";

        List<String> foundVariables = new ArrayList<String>();

        Matcher matcher = PatternConstants.PARAMETER_MATCHER.matcher(s);
        while (matcher.find()) {
            String paramName = matcher.group(1);

            foundVariables.add(paramName);
        }

        assertThat(foundVariables).hasSize(2).containsExactly(expected);
    }

    @Test
    public void testSingleVariableViaSysPropWithText() {

        String[] expected = new String[] { "System.getProperty(\"var1\",\"var1default\")" };
        String s = "#{System.getProperty(\"var1\",\"var1default\")}/someText";

        List<String> foundVariables = new ArrayList<String>();

        Matcher matcher = PatternConstants.PARAMETER_MATCHER.matcher(s);
        while (matcher.find()) {
            String paramName = matcher.group(1);

            foundVariables.add(paramName);
        }

        assertThat(foundVariables).hasSize(1).containsExactly(expected);
    }

    @Test
    public void testSingleVariableViaSysPropWithTextAndSpaces() {
        String[] expected = new String[] { "System.getProperty(\"var1\", \"var1default\")" };
        String s = "#{System.getProperty(\"var1\", \"var1default\")}/someText";

        List<String> foundVariables = new ArrayList<String>();

        Matcher matcher = PatternConstants.PARAMETER_MATCHER.matcher(s);
        while (matcher.find()) {
            String paramName = matcher.group(1);

            foundVariables.add(paramName);
        }

        assertThat(foundVariables).hasSize(1).containsExactly(expected);
    }

    @Test
    public void testMultiVariableViaSysPropWithText() {

        String[] expected = new String[] { "System.getProperty(\"var1\",\"var1default\")", "System.getProperty(\"var2\",\"var2default\")" };
        String s = "#{System.getProperty(\"var1\",\"var1default\")}/someText, #{System.getProperty(\"var2\",\"var2default\")}/someOtherText";

        List<String> foundVariables = new ArrayList<String>();

        Matcher matcher = PatternConstants.PARAMETER_MATCHER.matcher(s);
        while (matcher.find()) {
            String paramName = matcher.group(1);

            foundVariables.add(paramName);
        }

        assertThat(foundVariables).hasSize(2).containsExactly(expected);
    }

    @Test
    public void testMultiVariableViaSysPropWithTextAndSpaces() {

        String[] expected = new String[] { "System.getProperty(\"var1\", \"var1default\")", "System.getProperty(\"var2\", \"var2default\")" };
        String s = "#{System.getProperty(\"var1\", \"var1default\")}/someText, #{System.getProperty(\"var2\", \"var2default\")}/someOtherText";

        List<String> foundVariables = new ArrayList<String>();

        Matcher matcher = PatternConstants.PARAMETER_MATCHER.matcher(s);
        while (matcher.find()) {
            String paramName = matcher.group(1);

            foundVariables.add(paramName);
        }

        assertThat(foundVariables).hasSize(2).containsExactly(expected);
    }

}
