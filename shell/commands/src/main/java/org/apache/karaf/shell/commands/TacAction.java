/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.karaf.shell.commands;

import java.io.StringWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.OutputStream;
import java.io.BufferedWriter;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.karaf.shell.console.AbstractAction;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.gogo.commands.Option;
import org.apache.karaf.shell.console.CompletableFunction;
import org.apache.karaf.shell.console.commands.ComponentAction;

/**
 * Grab the text from the standard input and return it as a string.
 * Also write this text to a file if specified
 */
@Command(scope = TacAction.SCOPE_VALUE, name = TacAction.FUNCTION_VALUE, description = TacAction.DESCRIPTION)
@Component(name = TacAction.ID, description = TacAction.DESCRIPTION, immediate = true)
@Service(CompletableFunction.class)
@Properties({
        @Property(name = ComponentAction.SCOPE, value = TacAction.SCOPE_VALUE),
        @Property(name = ComponentAction.FUNCTION, value = TacAction.FUNCTION_VALUE)
})
public class TacAction extends ComponentAction {

    public static final String ID = "org.apache.karaf.shell.commands.tac";
    public static final String SCOPE_VALUE = "shell";
    public static final String FUNCTION_VALUE =  "tac";
    public static final String DESCRIPTION = "Captures the STDIN and returns it as a string. Optionally writes the content to a file.";


    @Option(name = "-f", aliases = {}, description = "Outputs the content to the given file", required = false, multiValued = false)
    private File file;

    public Object doExecute() throws Exception {
        StringWriter sw = new StringWriter();
        Writer[] writers;
        if (file != null) {
            writers = new Writer[] { sw, new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file))) };
        } else {
            writers = new Writer[] { sw };
        }
        BufferedReader rdr = new BufferedReader(new InputStreamReader(System.in));
        String s = rdr.readLine();
        boolean first = true;
        while (s != null)
        {
            for (Writer w : writers) {
                if (!first) {
                    w.write("\n");
                }
                w.write(s);
            }
            first = false;
            s = rdr.readLine();
        }
        for (Writer w : writers) {
            w.close();
        }
        return sw.toString();
    }
}
