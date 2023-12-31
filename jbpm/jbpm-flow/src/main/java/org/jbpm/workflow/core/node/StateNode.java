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
package org.jbpm.workflow.core.node;

import java.util.HashMap;
import java.util.Map;

import org.jbpm.workflow.core.Constraint;
import org.jbpm.workflow.core.impl.ConnectionRef;
import org.kie.api.definition.process.Connection;

public class StateNode extends CompositeContextNode implements Constrainable {

    private static final long serialVersionUID = 510l;

    private Map<ConnectionRef, Constraint> constraints = new HashMap<>();

    public void setConstraints(Map<ConnectionRef, Constraint> constraints) {
        this.constraints = constraints;
    }

    @Override
    public void setConstraint(final Connection connection, final Constraint constraint) {
        if (connection == null) {
            throw new IllegalArgumentException("connection is null");
        }
        if (!getDefaultOutgoingConnections().contains(connection)) {
            throw new IllegalArgumentException("connection is unknown:" + connection);
        }
        addConstraint(new ConnectionRef((String) connection.getMetaData().get("UniqueId"),
                connection.getTo().getId(), connection.getToType()), constraint);
    }

    @Override
    public void addConstraint(ConnectionRef connectionRef, Constraint constraint) {
        if (connectionRef == null) {
            throw new IllegalArgumentException(
                    "A state node only accepts constraints linked to a connection");
        }
        constraints.put(connectionRef, constraint);
    }

    public Constraint getConstraint(ConnectionRef connectionRef) {
        return constraints.get(connectionRef);
    }

    @Override
    public Map<ConnectionRef, Constraint> getConstraints() {
        return constraints;
    }

    @Override
    public Constraint getConstraint(final Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("connection is null");
        }
        ConnectionRef ref = new ConnectionRef((String) connection.getMetaData().get("UniqueId"), connection.getTo().getId(), connection.getToType());
        return this.constraints.get(ref);
    }

}
