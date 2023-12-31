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

import java.util.Date;

import org.jbpm.workflow.core.Node;
import org.jbpm.workflow.instance.impl.NodeInstanceImpl;
import org.kie.kogito.internal.process.runtime.KogitoNodeInstance;

public class ThrowLinkNodeInstance extends NodeInstanceImpl {

    private static final long serialVersionUID = 20110505L;

    @Override
    public void internalTrigger(KogitoNodeInstance from, String type) {
        triggerTime = new Date();
        this.triggerCompleted();
    }

    public void triggerCompleted() {
        this.triggerCompleted(
                Node.CONNECTION_DEFAULT_TYPE, true);
    }

}
