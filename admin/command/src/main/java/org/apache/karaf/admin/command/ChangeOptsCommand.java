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
package org.apache.karaf.admin.command;

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.karaf.shell.console.CompletableFunction;
import org.apache.karaf.shell.console.commands.ComponentAction;

@Command(scope = ChangeOptsCommand.SCOPE_VALUE, name = ChangeOptsCommand.FUNCTION_VALUE, description = ChangeOptsCommand.DESCRIPTION)
@Component(name = ChangeOptsCommand.ID, description = ChangeOptsCommand.DESCRIPTION, immediate = true)
@Service(CompletableFunction.class)
@Properties({
        @Property(name = ComponentAction.SCOPE, value = ChangeOptsCommand.SCOPE_VALUE),
        @Property(name = ComponentAction.FUNCTION, value = ChangeOptsCommand.FUNCTION_VALUE)
})
public class ChangeOptsCommand extends AdminCommandSupport {

    public static final String ID = "org.apache.karaf.admin.command.changeopts";
    public static final String SCOPE_VALUE = "admin";
    public static final String FUNCTION_VALUE =  "changeopts";
    public static final String DESCRIPTION = "Changes the Java options of an existing container instance.";

    @Argument(index = 0, name = "name", description="The name of the container instance", required = true, multiValued = false)
    private String instance = null;

    @Argument(index = 1, name = "javaOpts", description = "The new Java options to set", required = true, multiValued = false)
    private String javaOpts;

    public Object doExecute() throws Exception {
        getExistingInstance(instance).changeJavaOpts(javaOpts);
        return null;
    }
}
