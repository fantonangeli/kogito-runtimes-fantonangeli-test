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
package org.kie.kogito.incubation.processes.services.contexts;

import org.kie.kogito.incubation.common.DefaultCastable;
import org.kie.kogito.incubation.common.LocalId;
import org.kie.kogito.incubation.common.MetaDataContext;
import org.kie.kogito.incubation.processes.ProcessIdParser;

public class ProcessMetaDataContext implements DefaultCastable, MetaDataContext {
    private String id;

    private String businessKey;
    private String startFrom;

    protected ProcessMetaDataContext() {
    }

    public static ProcessMetaDataContext of(LocalId localId) {
        ProcessMetaDataContext mdc = new ProcessMetaDataContext();
        mdc.setId(localId);
        return mdc;
    }

    public <T extends LocalId> T id(Class<T> expected) {
        return ProcessIdParser.parse(id, expected);
    }

    String id() {
        return id;
    }

    public String businessKey() {
        return businessKey;
    }

    public String startFrom() {
        return startFrom;
    }

    void setId(String id) {
        this.id = id;
    }

    void setId(LocalId localId) {
        this.id = localId.asLocalUri().path();
    }

    void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    void setStartFrom(String startFrom) {
        this.startFrom = startFrom;
    }

    @Override
    public String toString() {
        return "ProcessMetaDataContext{" +
                "id='" + id + '\'' +
                '}';
    }
}