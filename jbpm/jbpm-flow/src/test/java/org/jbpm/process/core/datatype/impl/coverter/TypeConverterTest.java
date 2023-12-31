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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jbpm.process.core.datatype.impl.type.ObjectDataType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TypeConverterTest {

    @Test
    public void testStringObjectDataType() {

        ObjectDataType data = new ObjectDataType("java.lang.String");
        // no converted is used
        String readValue = (String) data.readValue("hello");
        assertThat(readValue).isEqualTo("hello");
    }

    @Test
    public void testDateObjectDataType() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        Date now = new Date();

        ObjectDataType data = new ObjectDataType("java.util.Date");
        // date converted is used
        Date readValue = (Date) data.readValue(sdf.format(now));
        assertThat(readValue).hasToString(now.toString());
    }
}
