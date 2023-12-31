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
package org.jbpm.workflow.instance.impl;

import java.util.Date;
import java.util.List;

import org.jbpm.process.instance.impl.Action;
import org.jbpm.workflow.core.DroolsAction;
import org.jbpm.workflow.core.impl.ExtendedNodeImpl;
import org.kie.kogito.internal.process.runtime.KogitoNodeInstance;

import static org.jbpm.ruleflow.core.Metadata.ACTION;
import static org.jbpm.workflow.core.Node.CONNECTION_DEFAULT_TYPE;
import static org.jbpm.workflow.core.impl.ExtendedNodeImpl.EVENT_NODE_ENTER;
import static org.jbpm.workflow.core.impl.ExtendedNodeImpl.EVENT_NODE_EXIT;

public abstract class ExtendedNodeInstanceImpl extends NodeInstanceImpl {

    private static final long serialVersionUID = 510l;

    public ExtendedNodeImpl getExtendedNode() {
        return (ExtendedNodeImpl) getNode();
    }

    public void internalTrigger(KogitoNodeInstance from, String type) {
        triggerTime = new Date();
        triggerEvent(EVENT_NODE_ENTER);
    }

    public void triggerCompleted(boolean remove) {
        triggerCompleted(CONNECTION_DEFAULT_TYPE, remove);
    }

    @Override
    public void triggerCompleted(String type, boolean remove) {
        triggerEvent(EVENT_NODE_EXIT);
        super.triggerCompleted(type, remove);
    }

    protected void triggerEvent(String type) {
        ExtendedNodeImpl extendedNode = getExtendedNode();
        if (extendedNode == null) {
            return;
        }
        List<DroolsAction> actions = extendedNode.getActions(type);
        if (actions != null) {
            for (DroolsAction droolsAction : actions) {
                Action action = (Action) droolsAction.getMetaData(ACTION);
                executeAction(action);
            }
        }
    }
}
