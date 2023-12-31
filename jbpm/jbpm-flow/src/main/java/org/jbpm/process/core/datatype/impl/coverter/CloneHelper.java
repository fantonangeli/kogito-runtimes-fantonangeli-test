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
package org.jbpm.process.core.datatype.impl.coverter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.UnaryOperator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CloneHelper {

    private static final Logger logger = LoggerFactory.getLogger(CloneHelper.class);
    private static final CloneHelper instance = new CloneHelper(CloneHelperRegister.get().getCloners());

    public static synchronized CloneHelper get() {
        return instance;
    }

    private Map<Class<?>, UnaryOperator<?>> cloners;
    private final Map<Class<?>, UnaryOperator<?>> registeredCloners;

    private CloneHelper(Map<Class<?>, UnaryOperator<?>> registeredCloners) {
        this.registeredCloners = registeredCloners;
        this.cloners = new ConcurrentHashMap<>(registeredCloners);
    }

    @SuppressWarnings("unchecked")
    public <T> T clone(T value) {
        return value == null ? value : getCloner((Class<T>) value.getClass()).apply(value);
    }

    @SuppressWarnings("unchecked")
    public <T> UnaryOperator<T> getCloner(Class<T> type) {
        return (UnaryOperator<T>) cloners.computeIfAbsent(type, this::searchCloner);
    }

    private UnaryOperator<?> searchCloner(Class<?> type) {
        return searchRegistered(type)
                .or(() -> searchCopyConstructor(type))
                .or(() -> searchCloneable(type))
                .orElse(o -> o);
    }

    private Optional<UnaryOperator<?>> searchRegistered(Class<?> type) {
        return registeredCloners.entrySet().stream().filter(e -> e.getKey().isAssignableFrom(type)).<UnaryOperator<?>> map(Entry::getValue).findFirst();
    }

    private Optional<UnaryOperator<?>> searchCloneable(Class<?> type) {
        return findCloneMethod(type).map(m -> handleException(m::invoke, type, "clone method"));
    }

    private Optional<UnaryOperator<?>> searchCopyConstructor(Class<?> type) {
        return findCopyConstructor(type).map(c -> handleException(c::newInstance, type, "copy constructor"));
    }

    @FunctionalInterface
    private interface CloneOperation {
        Object apply(Object o) throws ReflectiveOperationException;
    }

    private UnaryOperator<?> handleException(CloneOperation cloneOperation, Class<?> type, String message) {
        return o -> {
            try {
                return cloneOperation.apply(o);
            } catch (InvocationTargetException ex) {
                Throwable targetException = ex.getTargetException();
                if (targetException instanceof RuntimeException) {
                    throw (RuntimeException) targetException;
                } else {
                    throw new IllegalStateException("Invocation to " + message + " failed for " + type, targetException);
                }
            } catch (ReflectiveOperationException e) {
                logger.warn("Unexpected issue accessing existing {} for type {}, returning same instance. {}", message, type, e.getMessage());
                return o;
            }
        };
    }

    private Optional<Method> findCloneMethod(Class<?> type) {
        if (Cloneable.class.isAssignableFrom(type)) {
            try {
                return Optional.of(type.getMethod("clone"));
            } catch (NoSuchMethodException ex) {
                logger.warn("{} implements cloneable but clone method cannot be found", type);
            }
        }
        return Optional.empty();
    }

    private Optional<Constructor<?>> findCopyConstructor(Class<?> type) {
        try {
            return Optional.of(type.getConstructor(type));
        } catch (ReflectiveOperationException ex) {
            for (Constructor<?> constructor : type.getConstructors()) {
                if (constructor.getParameterCount() == 1 && constructor.getParameterTypes()[0].isAssignableFrom(type)) {
                    return Optional.of(constructor);
                }
            }
        }
        logger.debug("Cannot find copy constructor for type {}", type);
        return Optional.empty();
    }
}
