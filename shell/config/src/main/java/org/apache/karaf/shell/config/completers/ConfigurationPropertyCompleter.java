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

package org.apache.karaf.shell.config.completers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.felix.gogo.commands.Command;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.service.command.CommandSession;
import org.apache.karaf.shell.config.ConfigCommandSupport;
import org.apache.karaf.shell.console.Completer;
import org.apache.karaf.shell.console.completer.ArgumentCompleter;
import org.apache.karaf.shell.console.completer.StringsCompleter;
import org.apache.karaf.shell.console.jline.CommandSessionHolder;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

/**
 * {@link jline.Completor} for Configuration Admin properties.
 *
 * Displays a list of existing properties based on the current configuration being edited.
 *
 */
@Component(name = "org.apache.karaf.shell.config.completer.keys", immediate = true)
@Service(Completer.class)
@Properties(
        @Property(name = "completer.type", value = ConfigurationPropertyCompleter.COMPLETER_TYPE)
)
public class ConfigurationPropertyCompleter implements Completer {

    public static final String COMPLETER_TYPE = "config.keys";
    private final StringsCompleter delegate = new StringsCompleter();

    private static final String OPTION = "-p";
    private static final String ALIAS = "--pid";

    @Reference
    private ConfigurationAdmin configAdmin;

    public int complete(final String buffer, final int cursor, final List candidates) {
        CommandSession session = CommandSessionHolder.getSession();
        if (session != null) {
            String pid = getPid(session);
            Set<String> propertyNames = getPropertyNames(pid);
            delegate.getStrings().clear();
            if (propertyNames != null && !propertyNames.isEmpty()) {
                delegate.getStrings().addAll(propertyNames);
            }
        }
        return delegate.complete(buffer,cursor,candidates);
    }

    /**
     * Retrieves the pid stored in the {@link CommandSession} or passed as an argument.
     * Argument takes precedence from pid stored in the {@link CommandSession}.
     * @param commandSession
     * @return
     */
    private String getPid(CommandSession commandSession) {
        String pid = (String) commandSession.get(ConfigCommandSupport.PROPERTY_CONFIG_PID);
        ArgumentCompleter.ArgumentList list = (ArgumentCompleter.ArgumentList) commandSession.get(ArgumentCompleter.ARGUMENTS_LIST);
        if (list != null && list.getArguments() != null && list.getArguments().length > 0) {
            List<String> arguments = Arrays.asList(list.getArguments());
            if (arguments.contains(OPTION)) {
                int index = arguments.indexOf(OPTION);
                if (arguments.size() > index) {
                    return arguments.get(index + 1);
                }
            }

            if (arguments.contains(ALIAS)) {
                int index = arguments.indexOf(ALIAS);
                if (arguments.size() > index) {
                    return arguments.get(index + 1);
                }
            }
        }
        return pid;
    }

    /**
     * Returns the property names for the given pid.
     * @param pid
     * @return
     */
    private Set<String> getPropertyNames(String pid) {
        Set<String> propertyNames = new HashSet<String>();
        if (pid != null) {
            Configuration configuration = null;
            try {
                Configuration[] configs = configAdmin.listConfigurations("(service.pid="+pid+")");
                if (configs != null && configs.length > 0) {
                    configuration = configs[0];
                    if (configuration != null) {
                        Dictionary properties = configuration.getProperties();
                        if (properties != null) {
                            Enumeration keys = properties.keys();
                            while (keys.hasMoreElements()) {
                                propertyNames.add(String.valueOf(keys.nextElement()));
                            }
                        }
                    }
                }
            } catch (IOException e) {
              //Ignore
            } catch (InvalidSyntaxException e) {
                //Ignore
            }
        }
        return propertyNames;
    }

    public ConfigurationAdmin getConfigAdmin() {
        return configAdmin;
    }

    public void setConfigAdmin(ConfigurationAdmin configAdmin) {
        this.configAdmin = configAdmin;
    }
}
