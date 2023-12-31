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
package org.kie.kogito.jobs.service.api.schedule.timer;

import java.time.OffsetDateTime;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.kie.kogito.jobs.service.api.Schedule;
import org.kie.kogito.jobs.service.api.TemporalUnit;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import static org.kie.kogito.jobs.service.api.schedule.timer.TimerSchedule.DELAY_PROPERTY;
import static org.kie.kogito.jobs.service.api.schedule.timer.TimerSchedule.DELAY_UNIT_PROPERTY;
import static org.kie.kogito.jobs.service.api.schedule.timer.TimerSchedule.REPEAT_COUNT_PROPERTY;
import static org.kie.kogito.jobs.service.api.schedule.timer.TimerSchedule.START_TIME_PROPERTY;

@Schema(description = "Timer schedules establishes that a job must be executed at a given date time and can be repeated a configurable number of times.",
        allOf = { Schedule.class })
@JsonPropertyOrder({ START_TIME_PROPERTY, REPEAT_COUNT_PROPERTY, DELAY_PROPERTY, DELAY_UNIT_PROPERTY })
public class TimerSchedule extends Schedule {

    static final String START_TIME_PROPERTY = "startTime";
    static final String REPEAT_COUNT_PROPERTY = "repeatCount";
    static final String DELAY_PROPERTY = "delay";
    static final String DELAY_UNIT_PROPERTY = "delayUnit";

    @Schema(description = "Initial fire time for the job in the ISO-8601 standard.", example = "2023-01-30T12:01:15+01:00")
    private OffsetDateTime startTime;
    @Schema(description = "Number of times that the job execution must be repeated.", defaultValue = "0")
    private Integer repeatCount = 0;
    @Schema(description = "Time delay between executions.", defaultValue = "0")
    private Long delay = 0L;
    private TemporalUnit delayUnit = TemporalUnit.MILLIS;

    public TimerSchedule() {
        // Marshalling constructor.
    }

    public OffsetDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(OffsetDateTime startTime) {
        this.startTime = startTime;
    }

    public Integer getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(Integer repeatCount) {
        this.repeatCount = repeatCount;
    }

    public Long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    public TemporalUnit getDelayUnit() {
        return delayUnit;
    }

    public void setDelayUnit(TemporalUnit delayUnit) {
        this.delayUnit = delayUnit;
    }

    @Override
    public String toString() {
        return "TimerSchedule{" +
                "startTime='" + startTime + '\'' +
                ", repeatCount=" + repeatCount +
                ", delay=" + delay +
                ", delayUnit='" + delayUnit + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder(new TimerSchedule());
    }

    public static class Builder {

        private final TimerSchedule schedule;

        private Builder(TimerSchedule schedule) {
            this.schedule = schedule;
        }

        public Builder startTime(OffsetDateTime startTime) {
            schedule.setStartTime(startTime);
            return this;
        }

        public Builder repeatCount(Integer repeatCount) {
            schedule.setRepeatCount(repeatCount);
            return this;
        }

        public Builder delay(Long delay) {
            schedule.setDelay(delay);
            return this;
        }

        public Builder delayUnit(TemporalUnit delayUnit) {
            schedule.setDelayUnit(delayUnit);
            return this;
        }

        public TimerSchedule build() {
            return schedule;
        }
    }
}
