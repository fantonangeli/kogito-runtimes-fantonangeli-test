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
package org.kie.kogito.timer.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.kie.kogito.timer.JobHandle;

/**
 * A JobHandle container for scheduling multiple jobs
 */
public class MultiJobHandle extends AbstractJobHandle {

    private static final long serialVersionUID = 510l;

    private long id;

    private AtomicBoolean cancel = new AtomicBoolean(false);

    private final List<JobHandle> jobHandles;

    public MultiJobHandle(long id, List<JobHandle> jobHandles) {
        this.id = id;
        this.jobHandles = jobHandles;
    }

    public long getId() {
        return this.id;
    }

    public Object getJobHandles() {
        return jobHandles;
    }

    public void setCancel(boolean cancel) {
        for (JobHandle handle : jobHandles) {
            handle.setCancel(cancel);
        }
        this.cancel.set(cancel);
    }

    public boolean isCancel() {
        return cancel.get();
    }

}
