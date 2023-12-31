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
package org.jbpm.compiler.xml;

import java.io.StringReader;
import java.util.List;

import org.jbpm.compiler.xml.compiler.SemanticKnowledgeBuilderConfigurationImpl;
import org.jbpm.compiler.xml.core.SemanticModules;
import org.jbpm.process.core.impl.XmlProcessDumper;
import org.kie.api.definition.process.Process;
import org.kie.api.definition.process.WorkflowProcess;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.kogito.internal.process.runtime.KogitoWorkflowProcess;

public class XmlRuleFlowProcessDumper extends XmlWorkflowProcessDumper implements XmlProcessDumper {

    public static final XmlRuleFlowProcessDumper INSTANCE = new XmlRuleFlowProcessDumper();

    public XmlRuleFlowProcessDumper() {
        super(
                KogitoWorkflowProcess.RULEFLOW_TYPE,
                "http://drools.org/drools-5.0/process",
                "drools-processes-5.0.xsd",
                new ProcessSemanticModule());
    }

    public String dumpProcess(Process process) {
        return dump((WorkflowProcess) process, false);
    }

    @Override
    public Process readProcess(String processXml) {
        SemanticKnowledgeBuilderConfigurationImpl configuration = (SemanticKnowledgeBuilderConfigurationImpl) KnowledgeBuilderFactory.newKnowledgeBuilderConfiguration();
        SemanticModules modules = configuration.getSemanticModules();
        modules.addSemanticModule(new ProcessSemanticModule());
        XmlProcessReader xmlReader = new XmlProcessReader(modules, Thread.currentThread().getContextClassLoader());
        try {
            List<Process> processes = xmlReader.read(new StringReader(processXml));
            return processes.get(0);
        } catch (Exception t) {
            t.printStackTrace();
            return null;
        }
    }

}
