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
package org.kie.kogito.services.identity;

import java.util.Collections;
import java.util.List;

import org.kie.kogito.auth.IdentityProvider;

/**
 * @deprecated use IdentityProviders.of
 */
@Deprecated
public class StaticIdentityProvider implements IdentityProvider {

    private String name;
    private List<String> roles;

    public StaticIdentityProvider(String name) {
        this(name, Collections.emptyList());
    }

    public StaticIdentityProvider(String name, List<String> roles) {
        this.name = name;
        this.roles = roles == null ? Collections.emptyList() : roles;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getRoles() {
        return roles;
    }

    @Override
    public boolean hasRole(String role) {
        return roles.contains(role);
    }

}
