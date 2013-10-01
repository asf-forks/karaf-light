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
package org.apache.karaf.shell.dev;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import aQute.bnd.annotation.Deactivate;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.apache.karaf.shell.console.commands.ComponentAction;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.PackageAdmin;

/**
 * Base class for a dev: command that takes a bundle id as an argument
 *
 * It also provides convient access to the PackageAdmin service
 */
@Component(name = "org.apache.karaf.shell.dev.bundle.base", componentAbstract = true)
public abstract class AbstractBundleCommand extends ComponentAction {

    @Argument(index = 0, name = "id", description = "The bundle ID", required = true)
    Long id;

    @Reference
    private PackageAdmin admin;

    private BundleContext bundleContext;

    @Activate
    void activate(BundleContext bundleContext) {
        this.bundleContext = bundleContext;
    }

    @Deactivate
    void deactivate(){
    }

    @Override
    public Object doExecute() throws Exception {
        Bundle bundle = getBundleContext().getBundle(id);
        if (bundle == null) {
            System.err.println("Bundle ID " + id + " is invalid");
            return null;
        }

        doExecute(bundle);
        
        return null;
    }

    protected abstract void doExecute(Bundle bundle) throws Exception;

    /*
     * Get the list of bundles from which the given bundle imports packages
     */
    protected Map<String, Bundle> getWiredBundles(Bundle bundle) {
        // the set of bundles from which the bundle imports packages
        Map<String, Bundle> exporters = new HashMap<String, Bundle>();

        for (ExportedPackage pkg : getPackageAdmin().getExportedPackages((Bundle) null)) {
            Bundle[] bundles = pkg.getImportingBundles();
            if (bundles != null) {
                for (Bundle importingBundle : bundles) {
                    if (bundle.equals(importingBundle)
                            && !(pkg.getExportingBundle().getBundleId() == 0)
                            && !(pkg.getExportingBundle().equals(bundle))) {
                        exporters.put(pkg.getName(), pkg.getExportingBundle());
                    }
                }
            }
        }
        return exporters;
    }

    protected PackageAdmin getPackageAdmin() {
        return admin;    
    }

    protected BundleContext getBundleContext() {
        return bundleContext;
    }
}
