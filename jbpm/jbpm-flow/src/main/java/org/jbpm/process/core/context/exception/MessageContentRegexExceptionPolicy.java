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
package org.jbpm.process.core.context.exception;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.jbpm.process.core.context.exception.ExceptionHandlerPolicyUtils.isExceptionErrorCode;

public class MessageContentRegexExceptionPolicy implements ExceptionHandlerPolicy {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageContentRegexExceptionPolicy.class);

    @Override
    public boolean test(String errorCode, Throwable exception) {
        String msg = exception.getMessage();
        try {
            return msg != null && !isExceptionErrorCode(errorCode) && Pattern.compile(errorCode).matcher(msg).find();
        } catch (PatternSyntaxException ex) {
            LOGGER.debug("Failure parsing regular expression: {}", errorCode, ex);
        }
        return false;
    }
}
