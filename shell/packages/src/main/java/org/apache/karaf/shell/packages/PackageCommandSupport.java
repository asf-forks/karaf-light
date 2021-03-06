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
package org.apache.karaf.shell.packages;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.karaf.shell.console.commands.ComponentAction;
import org.osgi.service.packageadmin.PackageAdmin;

/**
 * Abstract class from which all commands related to the PackageAdmin
 * service should derive.
 * This command retrieves a reference to the PackageAdmin service before
 * calling another method to actually process the command.
 */
@Component(name = PackageCommandSupport.ID, componentAbstract = true)
public abstract class PackageCommandSupport extends ComponentAction {

    public static final String ID = "org.apache.karaf.shell.packages.base";

    @Reference
    private PackageAdmin packageAdmin;

    public Object doExecute() throws Exception {
        doExecute(packageAdmin);
        return null;
    }

    protected abstract void doExecute(PackageAdmin admin) throws Exception;
}
