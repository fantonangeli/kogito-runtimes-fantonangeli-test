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

import org.jbpm.workflow.core.Node;
import org.jbpm.workflow.core.node.Join;
import org.jbpm.workflow.core.node.Split;
import org.xml.sax.Attributes;

public class InclusiveGatewayHandler extends AbstractNodeHandler {

    protected Node createNode(Attributes attrs) {
        final String type = attrs.getValue("gatewayDirection");
        if ("Converging".equals(type)) {
            Join join = new Join();
            join.setType(Join.TYPE_OR);
            return join;
        } else if ("Diverging".equals(type)) {
            Split split = new Split();
            split.setType(Split.TYPE_OR);
            String isDefault = attrs.getValue("default");
            split.setMetaData("Default", isDefault);
            return split;
        } else {
            throw new ProcessParsingValidationException("Unknown gateway direction: " + type);
        }
    }

    @SuppressWarnings("unchecked")
    public Class generateNodeFor() {
        return Node.class;
    }

    public void writeNode(Node node, StringBuilder xmlDump, int metaDataType) {
        throw new IllegalArgumentException("Writing out should be handled by split / join handler");
    }

}
