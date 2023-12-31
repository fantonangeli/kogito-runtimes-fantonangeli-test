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
package org.kie.kogito.decision;

import java.util.Optional;

import org.kie.dmn.api.core.DMNContext;
import org.kie.kogito.ExecutionIdSupplier;

public class DecisionExecutionIdUtils {

    private DecisionExecutionIdUtils() {

    }

    private static final String EXECUTION_ID_KEY = "__kogito_execution_id__";

    public static String get(DMNContext context) {
        return getOptional(context).orElse(null);
    }

    public static Optional<String> getOptional(DMNContext context) {
        return Optional.ofNullable(context)
                .map(DMNContext::getMetadata)
                .map(metadata -> metadata.get(EXECUTION_ID_KEY))
                .map(Object::toString);
    }

    public static DMNContext inject(DMNContext context, ExecutionIdSupplier executionIdSupplier) {
        context.getMetadata().set(EXECUTION_ID_KEY, executionIdSupplier.get());
        return context;
    }
}
