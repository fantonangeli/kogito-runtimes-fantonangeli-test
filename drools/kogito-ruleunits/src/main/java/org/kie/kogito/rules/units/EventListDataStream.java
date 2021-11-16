/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.kogito.rules.units;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.drools.core.WorkingMemoryEntryPoint;
import org.drools.core.common.EventFactHandle;
import org.kie.api.time.SessionPseudoClock;
import org.kie.kogito.rules.DataProcessor;
import org.kie.kogito.rules.DataStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventListDataStream<T> implements DataStream<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventListDataStream.class);

    private final ArrayList<T> values = new ArrayList<>();
    private final List<DataProcessor> subscribers = new ArrayList<>();

    @SafeVarargs
    public static <T> EventListDataStream<T> create(T... ts) {
        EventListDataStream<T> stream = new EventListDataStream<>();
        for (T t : ts) {
            stream.append(t);
        }
        return stream;
    }

    @Override
    public void append(T t) {
        values.add(t);
        for (DataProcessor subscriber : subscribers) {
            insertAndAdvanceClock(t, subscriber);
        }
    }

    @Override
    public void subscribe(DataProcessor subscriber) {
        subscribers.add(subscriber);
        values.forEach(v -> insertAndAdvanceClock(v, subscriber));
    }

    private void insertAndAdvanceClock(T t, DataProcessor subscriber) {
        EventFactHandle fh = (EventFactHandle) subscriber.insert(null, t);
        long timestamp = fh.getStartTimestamp();
        WorkingMemoryEntryPoint ep = fh.getEntryPoint(null);
        SessionPseudoClock clock = (SessionPseudoClock) ep.getReteEvaluator().getSessionClock();
        long advanceTime = timestamp - clock.getCurrentTime();
        if (advanceTime > 0) {
            clock.advanceTime(advanceTime, TimeUnit.MILLISECONDS);
        } else if (advanceTime < 0) {
            LOGGER.warn("Received an event with a timestamp that is " + (-advanceTime) + " milliseconds in the past. " +
                    "Evaluation of out of order events could lead to unpredictable results.");
        }
    }
}