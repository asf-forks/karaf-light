/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.karaf.shell.commands;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

import junit.framework.TestCase;
import org.apache.felix.gogo.commands.basic.DefaultActionPreparator;

public class GrepTest extends TestCase {

    public void testGrep() throws Exception {
        InputStream input = System.in;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream("1\n2\n3\n4\n5\n6\n7\n8\n9\n".getBytes());
            System.setIn(bais);

            GrepAction grep = new GrepAction();
            DefaultActionPreparator preparator = new DefaultActionPreparator();
            preparator.prepare(grep, null, Arrays.<Object>asList("-C", "100", "2"));
            grep.doExecute();
        } finally {
            System.setIn(input);
        }
    }
}
