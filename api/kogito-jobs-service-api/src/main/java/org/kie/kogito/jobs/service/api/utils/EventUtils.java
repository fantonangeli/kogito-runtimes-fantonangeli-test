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
package org.kie.kogito.jobs.service.api.utils;

public class EventUtils {

    private EventUtils() {
    }

    public static boolean isValidExtensionName(String name) {
        if (name == null || name.length() == 0) {
            return false;
        }
        for (int i = 0; i < name.length(); i++) {
            if (!isValidExtensionChar(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static void validateExtensionName(String name) {
        if (!isValidExtensionName(name)) {
            throw new IllegalArgumentException("Invalid attribute or extension name: '" + name
                    + "'. CloudEvents extension and attribute names MUST consist of lower-case " +
                    " letters ('a' to 'z') or digits ('0' to '9') from the ASCII character set.");
        }
    }

    private static boolean isValidExtensionChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9');
    }
}
