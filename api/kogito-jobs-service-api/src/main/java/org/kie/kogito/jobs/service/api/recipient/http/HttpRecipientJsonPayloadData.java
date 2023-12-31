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
package org.kie.kogito.jobs.service.api.recipient.http;

import java.util.Objects;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

@Schema(allOf = { HttpRecipientPayloadData.class })
public class HttpRecipientJsonPayloadData extends HttpRecipientPayloadData<JsonNode> {

    @JsonProperty("data")
    private JsonNode dataJson;

    public HttpRecipientJsonPayloadData() {
        // Marshalling constructor.
    }

    private HttpRecipientJsonPayloadData(JsonNode data) {
        this.dataJson = data;
    }

    @Override
    public JsonNode getData() {
        return dataJson;
    }

    public static HttpRecipientJsonPayloadData from(JsonNode data) {
        return new HttpRecipientJsonPayloadData(data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HttpRecipientJsonPayloadData that = (HttpRecipientJsonPayloadData) o;
        return Objects.equals(dataJson, that.dataJson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataJson);
    }
}
