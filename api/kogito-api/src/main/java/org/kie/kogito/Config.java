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
package org.kie.kogito;

/**
 * Provides general configuration of Kogito application
 */
public interface Config {

    /**
     * Provides instance of requested KogitoConfig or null if not available
     * 
     * @param clazz clazz of the desired KogitoConfig
     * @return
     */
    <T extends KogitoConfig> T get(Class<T> clazz);

    /**
     * Provides access to addons in the application.
     *
     * @return addons available in the application
     */
    default Addons addons() {
        return Addons.EMTPY;
    }

}
