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
package org.jbpm.bpmn2.xml;

import org.jbpm.bpmn2.xml.di.BPMNEdgeHandler;
import org.jbpm.bpmn2.xml.di.BPMNPlaneHandler;
import org.jbpm.bpmn2.xml.di.BPMNShapeHandler;
import org.jbpm.compiler.xml.core.DefaultSemanticModule;

public class BPMNDISemanticModule extends DefaultSemanticModule {

    public BPMNDISemanticModule() {
        super("http://www.omg.org/spec/BPMN/20100524/DI");

        addHandler("BPMNPlane", new BPMNPlaneHandler());
        addHandler("BPMNShape", new BPMNShapeHandler());
        addHandler("BPMNEdge", new BPMNEdgeHandler());
    }

}
