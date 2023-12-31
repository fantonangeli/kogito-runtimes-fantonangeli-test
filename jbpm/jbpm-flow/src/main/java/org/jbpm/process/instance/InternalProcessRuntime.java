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
package org.jbpm.process.instance;

import org.drools.core.common.InternalKnowledgeRuntime;
import org.drools.core.common.InternalWorkingMemory;
import org.kie.api.runtime.process.ProcessRuntime;
import org.kie.kogito.Application;
import org.kie.kogito.internal.process.event.KogitoProcessEventSupport;
import org.kie.kogito.internal.process.runtime.KogitoProcessRuntime;
import org.kie.kogito.jobs.JobsService;
import org.kie.kogito.signal.SignalManager;
import org.kie.kogito.uow.UnitOfWorkManager;

public interface InternalProcessRuntime extends org.drools.core.runtime.process.InternalProcessRuntime, KogitoProcessRuntime.Provider {

    ProcessInstanceManager getProcessInstanceManager();

    SignalManager getSignalManager();

    KogitoProcessEventSupport getProcessEventSupport();

    UnitOfWorkManager getUnitOfWorkManager();

    InternalKnowledgeRuntime getInternalKieRuntime();

    JobsService getJobsService();

    Application getApplication();

    static KogitoProcessRuntime asKogitoProcessRuntime(ProcessRuntime kogitoProcessRuntimeProvider) {
        if (kogitoProcessRuntimeProvider instanceof KogitoProcessRuntime) {
            return (KogitoProcessRuntime) kogitoProcessRuntimeProvider;
        }
        if (kogitoProcessRuntimeProvider instanceof KogitoProcessRuntime.Provider) {
            return ((KogitoProcessRuntime.Provider) kogitoProcessRuntimeProvider).getKogitoProcessRuntime();
        }
        // this line is used only for legacy tests
        return ((KogitoProcessRuntime.Provider) ((InternalWorkingMemory) kogitoProcessRuntimeProvider).getProcessRuntime()).getKogitoProcessRuntime();
    }
}
