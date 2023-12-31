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
package org.kie.kogito.monitoring.core.common.system.metrics.dmnhandlers;

import java.util.Arrays;

import org.kie.kogito.KogitoGAV;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;

public class StringHandler implements TypeHandler<String> {

    private final String dmnType;
    private final KogitoGAV gav;
    private final MeterRegistry meterRegistry;

    public StringHandler(String dmnType, KogitoGAV gav, MeterRegistry meterRegistry) {
        this.dmnType = dmnType;
        this.gav = gav;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void record(String decision, String endpointName, String sample) {
        getCounter(decision, endpointName, sample).increment();
    }

    @Override
    public String getDmnType() {
        return dmnType;
    }

    private Counter getCounter(String decision, String endpoint, String identifier) {
        return Counter
                .builder(dmnType + DecisionConstants.DECISIONS_NAME_SUFFIX)
                .description(DecisionConstants.DECISIONS_HELP)
                .tags(Arrays.asList(Tag.of("decision", decision), Tag.of("endpoint", endpoint),
                        Tag.of("identifier", identifier), Tag.of("artifactId", gav.getArtifactId()),
                        Tag.of("version", gav.getVersion())))
                .register(meterRegistry);
    }
}
