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
package org.kie.kogito.grafana.model.functions;

import java.util.List;

public class SumByFunction implements GrafanaFunction {

    private static final String RENDER_TEMPLATE = "sum by (%s) (%s)";
    private GrafanaFunction grafanaFunction;
    private String sumByLabel;

    public SumByFunction(GrafanaFunction grafanaFunction, String sumByLabel) {
        this.grafanaFunction = grafanaFunction;
        this.sumByLabel = sumByLabel;
    }

    @Override
    public String render(String metricBody, List<Label> labels) {
        return String.format(RENDER_TEMPLATE, sumByLabel, grafanaFunction.render(metricBody, labels));
    }
}
