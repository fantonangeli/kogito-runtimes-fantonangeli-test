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
package org.jbpm.process.builder.dialect;

import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jbpm.process.builder.dialect.java.JavaProcessDialect;
import org.jbpm.process.builder.dialect.mvel.MVELProcessDialect;

public class ProcessDialectRegistry {

    private ProcessDialectRegistry() {

    }

    private static ConcurrentMap<String, ProcessDialect> dialects;

    static {
        dialects = new ConcurrentHashMap<>();
        dialects.put("java", new JavaProcessDialect());
        dialects.put("mvel", new MVELProcessDialect());
        ServiceLoader<ProcessDialectProvider> providers = ServiceLoader.load(ProcessDialectProvider.class);
        providers.forEach(provider -> dialects.put(provider.name(), provider.dialect()));

    }

    public static ProcessDialect getDialect(String dialect) {
        return dialects.get(dialect);
    }

    public static void setDialect(String dialectName, ProcessDialect dialect) {
        dialects.put(dialectName, dialect);
    }

}
