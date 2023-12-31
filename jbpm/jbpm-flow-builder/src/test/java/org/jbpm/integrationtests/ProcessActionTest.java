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
package org.jbpm.integrationtests;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.io.ReaderResource;
import org.jbpm.integrationtests.handler.TestWorkItemHandler;
import org.jbpm.integrationtests.test.Message;
import org.jbpm.test.util.AbstractBaseTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.kogito.internal.process.runtime.KogitoProcessInstance;
import org.kie.kogito.internal.process.runtime.KogitoProcessRuntime;
import org.kie.kogito.internal.process.runtime.KogitoWorkItem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class ProcessActionTest extends AbstractBaseTest {

    @Test
    @Disabled("On Exit not supported, see https://issues.redhat.com/browse/KOGITO-2067")
    public void testOnEntryExit() {
        Reader source = new StringReader(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<process xmlns=\"http://drools.org/drools-5.0/process\"\n" +
                        "         xmlns:xs=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xs:schemaLocation=\"http://drools.org/drools-5.0/process drools-processes-5.0.xsd\"\n" +
                        "         type=\"RuleFlow\" name=\"flow\" id=\"org.drools.actions\" package-name=\"org.drools\" version=\"1\" >\n" +
                        "\n" +
                        "  <header>\n" +
                        "    <globals>\n" +
                        "      <global identifier=\"list\" type=\"java.util.List\" />\n" +
                        "    </globals>\n" +
                        "  </header>\n" +
                        "\n" +
                        "  <nodes>\n" +
                        "    <start id=\"1\" name=\"Start\" />\n" +
                        "    <workItem id=\"2\" name=\"HumanTask\" >\n" +
                        "      <work name=\"Human Task\" >\n" +
                        "        <parameter name=\"ActorId\" >\n" +
                        "          <type name=\"org.jbpm.process.core.datatype.impl.type.StringDataType\" />\n" +
                        "          <value>John Doe</value>\n" +
                        "        </parameter>\n" +
                        "        <parameter name=\"TaskName\" >\n" +
                        "          <type name=\"org.jbpm.process.core.datatype.impl.type.StringDataType\" />\n" +
                        "          <value>Do something</value>\n" +
                        "        </parameter>\n" +
                        "        <parameter name=\"Priority\" >\n" +
                        "          <type name=\"org.jbpm.process.core.datatype.impl.type.StringDataType\" />\n" +
                        "        </parameter>\n" +
                        "        <parameter name=\"Comment\" >\n" +
                        "          <type name=\"org.jbpm.process.core.datatype.impl.type.StringDataType\" />\n" +
                        "        </parameter>\n" +
                        "      </work>\n" +
                        "      <onEntry>\n" +
                        "        <action type=\"expression\" name=\"Print\" dialect=\"mvel\" >list.add(\"Executing on entry action\");</action>\n" +
                        "      </onEntry>\n" +
                        "      <onExit>\n" +
                        "        <action type=\"expression\" name=\"Print\" dialect=\"java\" >list.add(\"Executing on exit action1\");</action>\n" +
                        "        <action type=\"expression\" name=\"Print\" dialect=\"java\" >list.add(\"Executing on exit action2\");</action>\n" +
                        "      </onExit>\n" +
                        "    </workItem>\n" +
                        "    <end id=\"3\" name=\"End\" />\n" +
                        "  </nodes>\n" +
                        "\n" +
                        "  <connections>\n" +
                        "    <connection from=\"1\" to=\"2\" />\n" +
                        "    <connection from=\"2\" to=\"3\" />\n" +
                        "  </connections>\n" +
                        "\n" +
                        "</process>");
        builder.add(new ReaderResource(source), ResourceType.DRF);
        KogitoProcessRuntime kruntime = createKogitoProcessRuntime();

        TestWorkItemHandler handler = new TestWorkItemHandler();
        kruntime.getKogitoWorkItemManager().registerWorkItemHandler("Human Task", handler);
        List<String> list = new ArrayList<String>();
        kruntime.getKieSession().setGlobal("list", list);
        KogitoProcessInstance kogitoProcessInstance = kruntime.startProcess("org.drools.actions");
        assertThat(kogitoProcessInstance.getState()).isEqualTo(KogitoProcessInstance.STATE_ACTIVE);
        KogitoWorkItem workItem = handler.getWorkItem();
        assertThat(workItem).isNotNull();
        assertThat(list).hasSize(1);
        kruntime.getKogitoWorkItemManager().completeWorkItem(workItem.getStringId(), null);
        assertThat(list).hasSize(3);
        assertThat(kogitoProcessInstance.getState()).isEqualTo(KogitoProcessInstance.STATE_COMPLETED);
    }

    @Test
    public void testActionContextJava() {
        Reader source = new StringReader(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<process xmlns=\"http://drools.org/drools-5.0/process\"\n" +
                        "         xmlns:xs=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xs:schemaLocation=\"http://drools.org/drools-5.0/process drools-processes-5.0.xsd\"\n" +
                        "         type=\"RuleFlow\" name=\"flow\" id=\"org.drools.actions\" package-name=\"org.drools\" version=\"1\" >\n" +
                        "\n" +
                        "  <header>\n" +
                        "    <imports>\n" +
                        "      <import name=\"org.jbpm.integrationtests.test.Message\" />\n" +
                        "    </imports>\n" +
                        "    <globals>\n" +
                        "      <global identifier=\"list\" type=\"java.util.List\" />\n" +
                        "    </globals>\n" +
                        "    <variables>\n" +
                        "      <variable name=\"variable\" >\n" +
                        "        <type name=\"org.jbpm.process.core.datatype.impl.type.StringDataType\" />\n" +
                        "        <value>SomeText</value>\n" +
                        "      </variable>\n" +
                        "    </variables>\n" +
                        "  </header>\n" +
                        "\n" +
                        "  <nodes>\n" +
                        "    <start id=\"1\" name=\"Start\" />\n" +
                        "    <actionNode id=\"2\" name=\"MyActionNode\" >\n" +
                        "      <action type=\"expression\" dialect=\"java\" >System.out.println(\"Triggered\");\n" +
                        "String myVariable = (String) kcontext.getVariable(\"variable\");\n" +
                        "list.add(myVariable);\n" +
                        "String nodeName = kcontext.getNodeInstance().getNodeName();\n" +
                        "list.add(nodeName);\n" +
                        "insert( new Message() );\n" +
                        "</action>\n" +
                        "    </actionNode>\n" +
                        "    <end id=\"3\" name=\"End\" />\n" +
                        "  </nodes>\n" +
                        "\n" +
                        "  <connections>\n" +
                        "    <connection from=\"1\" to=\"2\" />\n" +
                        "    <connection from=\"2\" to=\"3\" />\n" +
                        "  </connections>\n" +
                        "\n" +
                        "</process>");
        builder.add(new ReaderResource(source), ResourceType.DRF);
        KogitoProcessRuntime kruntime = createKogitoProcessRuntime();
        List<String> list = new ArrayList<String>();
        kruntime.getKieSession().setGlobal("list", list);
        KogitoProcessInstance kogitoProcessInstance = kruntime.startProcess("org.drools.actions");
        assertThat(list).hasSize(2);
        assertThat(list.get(0)).isEqualTo("SomeText");
        assertThat(list.get(1)).isEqualTo("MyActionNode");
        Collection<FactHandle> factHandles = kruntime.getKieSession().getFactHandles(object -> object instanceof Message);
        assertThat(factHandles).isNotEmpty();
        assertThat(kogitoProcessInstance.getState()).isEqualTo(KogitoProcessInstance.STATE_COMPLETED);
    }

    @Test
    @Disabled("MVEL not supported in ScriptTask")
    public void testActionContextMVEL() {
        Reader source = new StringReader(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<process xmlns=\"http://drools.org/drools-5.0/process\"\n" +
                        "         xmlns:xs=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xs:schemaLocation=\"http://drools.org/drools-5.0/process drools-processes-5.0.xsd\"\n" +
                        "         type=\"RuleFlow\" name=\"flow\" id=\"org.drools.actions\" package-name=\"org.drools\" version=\"1\" >\n" +
                        "\n" +
                        "  <header>\n" +
                        "    <imports>\n" +
                        "      <import name=\"org.jbpm.integrationtests.test.Message\" />\n" +
                        "    </imports>\n" +
                        "    <globals>\n" +
                        "      <global identifier=\"list\" type=\"java.util.List\" />\n" +
                        "    </globals>\n" +
                        "    <variables>\n" +
                        "      <variable name=\"variable\" >\n" +
                        "        <type name=\"org.jbpm.process.core.datatype.impl.type.StringDataType\" />\n" +
                        "        <value>SomeText</value>\n" +
                        "      </variable>\n" +
                        "    </variables>\n" +
                        "  </header>\n" +
                        "\n" +
                        "  <nodes>\n" +
                        "    <start id=\"1\" name=\"Start\" />\n" +
                        "    <actionNode id=\"2\" name=\"MyActionNode\" >\n" +
                        "      <action type=\"expression\" dialect=\"mvel\" >System.out.println(\"Triggered\");\n" +
                        "System.out.println(kcontext.getKieRuntime());\n" +
                        "String myVariable = (String) kcontext.getVariable(\"variable\");\n" +
                        "list.add(myVariable);\n" +
                        "String nodeName = kcontext.getNodeInstance().getNodeName();\n" +
                        "list.add(nodeName);\n" +
                        "insert( new Message() );\n" +
                        "</action>\n" +
                        "    </actionNode>\n" +
                        "    <end id=\"3\" name=\"End\" />\n" +
                        "  </nodes>\n" +
                        "\n" +
                        "  <connections>\n" +
                        "    <connection from=\"1\" to=\"2\" />\n" +
                        "    <connection from=\"2\" to=\"3\" />\n" +
                        "  </connections>\n" +
                        "\n" +
                        "</process>");
        builder.add(new ReaderResource(source), ResourceType.DRF);
        if (builder.hasErrors()) {
            fail(builder.getErrors().toString());
        }
        KogitoProcessRuntime kruntime = createKogitoProcessRuntime();
        List<String> list = new ArrayList<String>();
        kruntime.getKieSession().setGlobal("list", list);
        KogitoProcessInstance kogitoProcessInstance = kruntime.startProcess("org.drools.actions");
        assertThat(list).hasSize(2);
        assertThat(list.get(0)).isEqualTo("SomeText");
        assertThat(list.get(1)).isEqualTo("MyActionNode");
        Collection<FactHandle> factHandles = kruntime.getKieSession().getFactHandles(object -> object instanceof Message);
        assertThat(factHandles).isNotEmpty();
        assertThat(kogitoProcessInstance.getState()).isEqualTo(KogitoProcessInstance.STATE_COMPLETED);
    }

    @Test
    public void testActionVariableJava() {
        Reader source = new StringReader(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<process xmlns=\"http://drools.org/drools-5.0/process\"\n" +
                        "         xmlns:xs=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xs:schemaLocation=\"http://drools.org/drools-5.0/process drools-processes-5.0.xsd\"\n" +
                        "         type=\"RuleFlow\" name=\"flow\" id=\"org.drools.actions\" package-name=\"org.drools\" version=\"1\" >\n" +
                        "\n" +
                        "  <header>\n" +
                        "    <imports>\n" +
                        "      <import name=\"org.jbpm.integrationtests.TestVariable\" />\n" +
                        "    </imports>\n" +
                        "    <globals>\n" +
                        "      <global identifier=\"list\" type=\"java.util.List\" />\n" +
                        "    </globals>\n" +
                        "    <variables>\n" +
                        "      <variable name=\"person\" >\n" +
                        "        <type name=\"org.jbpm.process.core.datatype.impl.type.ObjectDataType\" className=\"org.jbpm.integrationtests.TestVariable\" />\n" +
                        "      </variable>\n" +
                        "    </variables>\n" +
                        "  </header>\n" +
                        "\n" +
                        "  <nodes>\n" +
                        "    <start id=\"1\" name=\"Start\" />\n" +
                        "    <actionNode id=\"2\" name=\"MyActionNode\" >\n" +
                        "      <action type=\"expression\" dialect=\"java\" >System.out.println(\"Triggered\");\n" +
                        "list.add(person.getName());\n" +
                        "</action>\n" +
                        "    </actionNode>\n" +
                        "    <end id=\"3\" name=\"End\" />\n" +
                        "  </nodes>\n" +
                        "\n" +
                        "  <connections>\n" +
                        "    <connection from=\"1\" to=\"2\" />\n" +
                        "    <connection from=\"2\" to=\"3\" />\n" +
                        "  </connections>\n" +
                        "\n" +
                        "</process>");
        builder.add(new ReaderResource(source), ResourceType.DRF);
        KogitoProcessRuntime kruntime = createKogitoProcessRuntime();
        List<String> list = new ArrayList<String>();
        kruntime.getKieSession().setGlobal("list", list);
        TestVariable person = new TestVariable("John Doe");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("person", person);
        KogitoProcessInstance kogitoProcessInstance =
                kruntime.startProcess("org.drools.actions", params);
        assertThat(list).hasSize(1);
        assertThat(list.get(0)).isEqualTo("John Doe");
        assertThat(kogitoProcessInstance.getState()).isEqualTo(KogitoProcessInstance.STATE_COMPLETED);
    }

    @Test
    @Disabled("MVEL not supported in ScriptTask")
    public void testActionVariableMVEL() {
        Reader source = new StringReader(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<process xmlns=\"http://drools.org/drools-5.0/process\"\n" +
                        "         xmlns:xs=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xs:schemaLocation=\"http://drools.org/drools-5.0/process drools-processes-5.0.xsd\"\n" +
                        "         type=\"RuleFlow\" name=\"flow\" id=\"org.drools.actions\" package-name=\"org.drools\" version=\"1\" >\n" +
                        "\n" +
                        "  <header>\n" +
                        "    <imports>\n" +
                        "      <import name=\"org.jbpm.integrationtests.TestVariable\" />\n" +
                        "    </imports>\n" +
                        "    <globals>\n" +
                        "      <global identifier=\"list\" type=\"java.util.List\" />\n" +
                        "    </globals>\n" +
                        "    <variables>\n" +
                        "      <variable name=\"person\" >\n" +
                        "        <type name=\"org.jbpm.process.core.datatype.impl.type.ObjectDataType\" className=\"org.jbpm.integrationtests.TestVariable\" />\n" +
                        "      </variable>\n" +
                        "    </variables>\n" +
                        "  </header>\n" +
                        "\n" +
                        "  <nodes>\n" +
                        "    <start id=\"1\" name=\"Start\" />\n" +
                        "    <actionNode id=\"2\" name=\"MyActionNode\" >\n" +
                        "      <action type=\"expression\" dialect=\"mvel\" >System.out.println(\"Triggered\");\n" +
                        "list.add(person.name);\n" +
                        "</action>\n" +
                        "    </actionNode>\n" +
                        "    <end id=\"3\" name=\"End\" />\n" +
                        "  </nodes>\n" +
                        "\n" +
                        "  <connections>\n" +
                        "    <connection from=\"1\" to=\"2\" />\n" +
                        "    <connection from=\"2\" to=\"3\" />\n" +
                        "  </connections>\n" +
                        "\n" +
                        "</process>");
        builder.add(new ReaderResource(source), ResourceType.DRF);
        KogitoProcessRuntime kruntime = createKogitoProcessRuntime();
        List<String> list = new ArrayList<>();
        kruntime.getKieSession().setGlobal("list", list);
        TestVariable person = new TestVariable("John Doe");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("person", person);
        KogitoProcessInstance kogitoProcessInstance =
                kruntime.startProcess("org.drools.actions", params);
        assertThat(list).hasSize(1);
        assertThat(list.get(0)).isEqualTo("John Doe");
        assertThat(kogitoProcessInstance.getState()).isEqualTo(KogitoProcessInstance.STATE_COMPLETED);
    }

    @Test
    public void testActionNameConflict() {
        Reader source = new StringReader(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<process xmlns=\"http://drools.org/drools-5.0/process\"\n" +
                        "         xmlns:xs=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xs:schemaLocation=\"http://drools.org/drools-5.0/process drools-processes-5.0.xsd\"\n" +
                        "         type=\"RuleFlow\" name=\"flow\" id=\"org.drools.actions1\" package-name=\"org.drools\" version=\"1\" >\n" +
                        "\n" +
                        "  <header>\n" +
                        "    <globals>\n" +
                        "      <global identifier=\"list\" type=\"java.util.List\" />\n" +
                        "    </globals>\n" +
                        "  </header>\n" +
                        "\n" +
                        "  <nodes>\n" +
                        "    <start id=\"1\" name=\"Start\" />\n" +
                        "    <actionNode id=\"2\" name=\"MyActionNode\" >\n" +
                        "      <action type=\"expression\" dialect=\"java\" >list.add(\"Action1\");</action>\n" +
                        "    </actionNode>\n" +
                        "    <end id=\"3\" name=\"End\" />\n" +
                        "  </nodes>\n" +
                        "\n" +
                        "  <connections>\n" +
                        "    <connection from=\"1\" to=\"2\" />\n" +
                        "    <connection from=\"2\" to=\"3\" />\n" +
                        "  </connections>\n" +
                        "\n" +
                        "</process>");
        builder.add(new ReaderResource(source), ResourceType.DRF);
        source = new StringReader(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<process xmlns=\"http://drools.org/drools-5.0/process\"\n" +
                        "         xmlns:xs=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xs:schemaLocation=\"http://drools.org/drools-5.0/process drools-processes-5.0.xsd\"\n" +
                        "         type=\"RuleFlow\" name=\"flow\" id=\"org.drools.actions2\" package-name=\"org.drools\" version=\"1\" >\n" +
                        "\n" +
                        "  <header>\n" +
                        "    <globals>\n" +
                        "      <global identifier=\"list\" type=\"java.util.List\" />\n" +
                        "    </globals>\n" +
                        "  </header>\n" +
                        "\n" +
                        "  <nodes>\n" +
                        "    <start id=\"1\" name=\"Start\" />\n" +
                        "    <actionNode id=\"2\" name=\"MyActionNode\" >\n" +
                        "      <action type=\"expression\" dialect=\"java\" >list.add(\"Action2\");</action>\n" +
                        "    </actionNode>\n" +
                        "    <end id=\"3\" name=\"End\" />\n" +
                        "  </nodes>\n" +
                        "\n" +
                        "  <connections>\n" +
                        "    <connection from=\"1\" to=\"2\" />\n" +
                        "    <connection from=\"2\" to=\"3\" />\n" +
                        "  </connections>\n" +
                        "\n" +
                        "</process>");
        builder.add(new ReaderResource(source), ResourceType.DRF);
        KogitoProcessRuntime kruntime = createKogitoProcessRuntime();
        List<String> list = new ArrayList<String>();
        kruntime.getKieSession().setGlobal("list", list);
        KogitoProcessInstance kogitoProcessInstance =
                kruntime.startProcess("org.drools.actions1");
        assertThat(list).hasSize(1);
        assertThat(list.get(0)).isEqualTo("Action1");
        list.clear();
        kogitoProcessInstance = kruntime.startProcess("org.drools.actions2");
        assertThat(list).hasSize(1);
        assertThat(list.get(0)).isEqualTo("Action2");
        assertThat(kogitoProcessInstance.getState()).isEqualTo(KogitoProcessInstance.STATE_COMPLETED);
    }

    @Test
    public void testActionContextJavaBackwardCheck() {
        Reader source = new StringReader(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<process xmlns=\"http://drools.org/drools-5.0/process\"\n" +
                        "         xmlns:xs=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                        "         xs:schemaLocation=\"http://drools.org/drools-5.0/process drools-processes-5.0.xsd\"\n" +
                        "         type=\"RuleFlow\" name=\"flow\" id=\"org.drools.actions\" package-name=\"org.drools\" version=\"1\" >\n" +
                        "\n" +
                        "  <header>\n" +
                        "    <imports>\n" +
                        "      <import name=\"org.jbpm.integrationtests.test.Message\" />\n" +
                        "    </imports>\n" +
                        "    <globals>\n" +
                        "      <global identifier=\"list\" type=\"java.util.List\" />\n" +
                        "    </globals>\n" +
                        "    <variables>\n" +
                        "      <variable name=\"variable\" >\n" +
                        "        <type name=\"org.drools.core.process.core.datatype.impl.type.StringDataType\" />\n" +
                        "        <value>SomeText</value>\n" +
                        "      </variable>\n" +
                        "    </variables>\n" +
                        "  </header>\n" +
                        "\n" +
                        "  <nodes>\n" +
                        "    <start id=\"1\" name=\"Start\" />\n" +
                        "    <actionNode id=\"2\" name=\"MyActionNode\" >\n" +
                        "      <action type=\"expression\" dialect=\"java\" >System.out.println(\"Triggered\");\n" +
                        "String myVariable = (String) kcontext.getVariable(\"variable\");\n" +
                        "list.add(myVariable);\n" +
                        "String nodeName = kcontext.getNodeInstance().getNodeName();\n" +
                        "list.add(nodeName);\n" +
                        "insert( new Message() );\n" +
                        "</action>\n" +
                        "    </actionNode>\n" +
                        "    <end id=\"3\" name=\"End\" />\n" +
                        "  </nodes>\n" +
                        "\n" +
                        "  <connections>\n" +
                        "    <connection from=\"1\" to=\"2\" />\n" +
                        "    <connection from=\"2\" to=\"3\" />\n" +
                        "  </connections>\n" +
                        "\n" +
                        "</process>");
        builder.add(new ReaderResource(source), ResourceType.DRF);
        KogitoProcessRuntime kruntime = createKogitoProcessRuntime();
        List<String> list = new ArrayList<String>();
        kruntime.getKieSession().setGlobal("list", list);
        KogitoProcessInstance kogitoProcessInstance = kruntime.startProcess("org.drools.actions");
        assertThat(list).hasSize(2);
        assertThat(list.get(0)).isEqualTo("SomeText");
        assertThat(list.get(1)).isEqualTo("MyActionNode");
        Collection<FactHandle> factHandles = kruntime.getKieSession().getFactHandles(object -> object instanceof Message);
        assertThat(factHandles).isNotEmpty();
        assertThat(kogitoProcessInstance.getState()).isEqualTo(KogitoProcessInstance.STATE_COMPLETED);
    }

}
