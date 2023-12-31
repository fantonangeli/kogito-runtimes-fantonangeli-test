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
package org.kie.kogito.monitoring.core.common.system.metrics;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.kie.kogito.KogitoGAV;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;

public class SystemMetricsCollector {

    private static final String STATUS_CODE_NAME = "api_http_response_code";

    private static final String STATUS_CODE_HELP = "Request status code.";

    private static final String ELAPSED_TIME_NAME = "api_execution_elapsed";

    private static final double[] ELAPSED_TIME_PERCENTILES = new double[] { 0.1, 0.25, 0.5, 0.75, 0.9, 0.99 };

    private static final String ELAPSED_TIME_HELP = "Endpoint execution elapsed seconds, 3 minutes time window.";

    private static final String EXCEPTIONS_NAME = "api_http_stacktrace_exceptions";

    private static final String EXCEPTIONS_HELP = "System exceptions details.";

    private final MeterRegistry registry;

    private final KogitoGAV gav;

    public SystemMetricsCollector(KogitoGAV gav, MeterRegistry meterRegistry) {
        this.gav = gav;
        this.registry = meterRegistry;
    }

    private Counter getRequestStatusCodeCounter(String endpoint, String identifier) {
        return Counter.builder(STATUS_CODE_NAME)
                .description(STATUS_CODE_HELP)
                .tags(Arrays.asList(Tag.of("endpoint", endpoint), Tag.of("identifier", identifier), Tag.of("artifactId", gav.getArtifactId()), Tag.of("version", gav.getVersion())))
                .register(registry);
    }

    private Counter getExceptionsCounter(String endpoint, String identifier) {
        return Counter.builder(EXCEPTIONS_NAME)
                .description(EXCEPTIONS_HELP)
                .tags(Arrays.asList(Tag.of("endpoint", endpoint), Tag.of("identifier", identifier), Tag.of("artifactId", gav.getArtifactId()), Tag.of("version", gav.getVersion())))
                .register(registry);
    }

    private Timer getElapsedTimeSummary(String endpoint) {
        return Timer.builder(ELAPSED_TIME_NAME)
                .description(ELAPSED_TIME_HELP)
                .publishPercentiles(ELAPSED_TIME_PERCENTILES)
                .distributionStatisticExpiry(Duration.ofMinutes(3))
                .tags(Arrays.asList(Tag.of("endpoint", endpoint), Tag.of("artifactId", gav.getArtifactId()), Tag.of("version", gav.getVersion())))
                .register(registry);
    }

    public void registerStatusCodeRequest(String endpoint, String statusCode) {
        getRequestStatusCodeCounter(endpoint, statusCode).increment();
    }

    public void registerElapsedTimeSampleMetrics(String endpoint, long elapsedTime) {
        getElapsedTimeSummary(endpoint).record(elapsedTime, TimeUnit.NANOSECONDS);
    }

    public void registerException(String endpoint, String stackTrace) {
        getExceptionsCounter(endpoint, stackTrace).increment();
    }
}
