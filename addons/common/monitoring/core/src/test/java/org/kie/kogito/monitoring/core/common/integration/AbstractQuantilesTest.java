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
package org.kie.kogito.monitoring.core.common.integration;

import org.kie.kogito.monitoring.core.common.system.metrics.dmnhandlers.DecisionConstants;
import org.kie.kogito.monitoring.core.common.system.metrics.dmnhandlers.TypeHandler;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

public abstract class AbstractQuantilesTest<T extends TypeHandler> {

    public static final String[] INTERNAL_PROMETHEUS_LABELS =
            new String[] {
                    DecisionConstants.DECISION_ENDPOINT_IDENTIFIER_LABELS[0],
                    DecisionConstants.DECISION_ENDPOINT_IDENTIFIER_LABELS[1],
                    "quantile"
            };
    protected static final String ENDPOINT_NAME = "hello";
    protected SimpleMeterRegistry registry;
    protected T handler;
    protected String dmnType;
}
