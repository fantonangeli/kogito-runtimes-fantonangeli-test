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
package org.jbpm.compiler.canonical.descriptors;

import java.util.function.Supplier;

import org.jbpm.process.instance.impl.ExpressionReturnValueEvaluator;

import com.github.javaparser.ast.expr.Expression;

public class ExpressionReturnValueSupplier extends ExpressionReturnValueEvaluator implements Supplier<Expression> {

    private final Expression expression;

    public ExpressionReturnValueSupplier(String lang, String expr, String rootName) {
        super(lang, expr, rootName);
        expression = ExpressionUtils.getObjectCreationExpr(ExpressionReturnValueEvaluator.class, lang, expr, rootName);
    }

    @Override
    public Expression get() {
        return expression;
    }

}
