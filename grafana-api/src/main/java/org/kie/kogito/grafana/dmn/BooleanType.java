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
package org.kie.kogito.grafana.dmn;

import org.kie.kogito.grafana.model.functions.BaseExpression;
import org.kie.kogito.grafana.model.functions.IncreaseFunction;
import org.kie.kogito.grafana.model.functions.SumByFunction;

public class BooleanType extends AbstractDmnType {

    private static final String DMN_TYPE = "boolean";
    private static final String NAME_SUFFIX = "total";

    public BooleanType() {
        super(Boolean.class, DMN_TYPE, NAME_SUFFIX);
        BaseExpression baseExpression = new BaseExpression(DMN_TYPE, NAME_SUFFIX);
        setGrafanaFunction(new SumByFunction(new IncreaseFunction(baseExpression, "1m"), "identifier"));
    }
}
