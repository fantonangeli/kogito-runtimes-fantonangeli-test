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
package org.jbpm.compiler.xml.compiler.rules;

import org.drools.drl.ast.descr.ConnectiveDescr;
import org.drools.drl.ast.descr.ConnectiveDescr.RestrictionConnectiveType;
import org.drools.drl.ast.descr.ExprConstraintDescr;
import org.drools.drl.ast.descr.PatternDescr;
import org.drools.drl.ast.descr.RestrictionConnectiveDescr;
import org.jbpm.compiler.xml.Handler;
import org.jbpm.compiler.xml.Parser;
import org.jbpm.compiler.xml.core.BaseAbstractHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class RestrictionConnectiveHandler extends BaseAbstractHandler
        implements
        Handler {

    public static final String AND = "and-";
    public static final String OR = "or-";

    public RestrictionConnectiveHandler() {
    }

    public Object start(final String uri,
            final String localName,
            final Attributes attrs,
            final Parser parser) throws SAXException {
        parser.startElementBuilder(localName,
                attrs);

        if (localName.startsWith(RestrictionConnectiveHandler.OR)) {
            return new ConnectiveDescr(RestrictionConnectiveType.OR);
        } else if (localName.startsWith(RestrictionConnectiveHandler.AND)) {
            return new ConnectiveDescr(RestrictionConnectiveType.AND);
        } else {
            throw new SAXParseException("<" + localName + "> should have'",
                    parser.getLocator());
        }

    }

    public Object end(final String uri,
            final String localName,
            final Parser parser) throws SAXException {
        parser.endElementBuilder();
        Object op = parser.getParent();
        ConnectiveDescr c = (ConnectiveDescr) parser.getCurrent();

        if (op instanceof PatternDescr) {
            StringBuilder sb = new StringBuilder();
            c.buildExpression(sb);

            ExprConstraintDescr expr = new ExprConstraintDescr();
            expr.setExpression(sb.toString());

            final PatternDescr patternDescr = (PatternDescr) op;
            patternDescr.addConstraint(expr);
        } else {
            ConnectiveDescr p = (ConnectiveDescr) op;
            p.add(c);
        }
        return c;
    }

    public Class generateNodeFor() {
        return RestrictionConnectiveDescr.class;
    }
}
