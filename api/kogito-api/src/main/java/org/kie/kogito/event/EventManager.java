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
package org.kie.kogito.event;

import org.kie.kogito.Addons;

/**
 * Event manager that is entry point for handling events generated
 * during execution.
 *
 */
public interface EventManager {

    /**
     * Returns new batch instance, that should be used just by one
     * processing thread
     * 
     * @return new batch instance
     */
    EventBatch newBatch();

    /**
     * Publishes events of the batch with main restriction that the batch is
     * processed only when there are any publishers available.
     * 
     * @param batch batch to be published
     */
    void publish(EventBatch batch);

    /**
     * Adds given publisher to the event manager's list of publishers.
     * Multiple publishers can be added and each will be invoked with exact same events.
     * 
     * @param publisher publisher to be added
     */
    void addPublisher(EventPublisher publisher);

    /**
     * Sets the service information that will be attached to events as source.
     * This is expected to be URL like structure that will allow consumer of the
     * events to navigate back.
     * 
     * @param service endpoint of the service
     */
    void setService(String service);

    /**
     * Optionally adds available addons in the running service
     * 
     * @param addons addons available in the service
     */
    void setAddons(Addons addons);
}
