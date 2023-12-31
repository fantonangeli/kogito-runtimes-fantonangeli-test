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
package org.jbpm.compiler.xml.core;

import java.util.Set;

import org.jbpm.compiler.xml.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public abstract class BaseAbstractHandler {
    protected Set<Class<?>> validPeers;
    protected Set<Class<?>> validParents;
    protected boolean allowNesting;

    public Set<Class<?>> getValidParents() {
        return this.validParents;
    }

    public Set<Class<?>> getValidPeers() {
        return this.validPeers;
    }

    public boolean allowNesting() {
        return this.allowNesting;
    }

    public void emptyAttributeCheck(final String element,
            final String attributeName,
            final String attribute,
            final Parser xmlPackageReader) throws SAXException {
        if (attribute == null || attribute.trim().equals("")) {
            throw new SAXParseException("<" + element + "> requires a '" + attributeName + "' attribute",
                    xmlPackageReader.getLocator());
        }
    }

    public void emptyContentCheck(final String element,
            final String content,
            final Parser xmlPackageReader) throws SAXException {
        if (content == null || content.trim().equals("")) {
            throw new SAXParseException("<" + element + "> requires content",
                    xmlPackageReader.getLocator());
        }
    }
}
