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

import java.util.Arrays;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;

@Schema(allOf = { HttpRecipientPayloadData.class })
public class HttpRecipientBinaryPayloadData extends HttpRecipientPayloadData<byte[]> {

    @JsonProperty("data")
    private byte[] dataBytes;

    public HttpRecipientBinaryPayloadData() {
        // Marshalling constructor.
    }

    private HttpRecipientBinaryPayloadData(byte[] data) {
        this.dataBytes = data;
    }

    @Override
    public byte[] getData() {
        return dataBytes;
    }

    public static HttpRecipientBinaryPayloadData from(byte[] data) {
        return new HttpRecipientBinaryPayloadData(data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HttpRecipientBinaryPayloadData)) {
            return false;
        }
        HttpRecipientBinaryPayloadData that = (HttpRecipientBinaryPayloadData) o;
        return Arrays.equals(dataBytes, that.dataBytes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(dataBytes);
    }
}
