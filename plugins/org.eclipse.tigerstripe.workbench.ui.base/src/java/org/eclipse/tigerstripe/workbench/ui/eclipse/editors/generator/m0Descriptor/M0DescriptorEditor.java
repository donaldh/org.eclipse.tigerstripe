/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.generator.m0Descriptor;

import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.LicensedAccess;
import org.eclipse.tigerstripe.workbench.internal.core.util.license.TSWorkbenchPluggablePluginRole;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.generator.GeneratorDescriptorEditor;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.generator.GeneratorDescriptorErrorPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.generator.m0Descriptor.header.M0OverviewPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.generator.m0Descriptor.rules.M0DescriptorRulesPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.properties.PluginDescriptorPropertiesPage;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.pluginDescriptor.runtime.RuntimePage;
import org.eclipse.ui.PartInitException;

/**
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class M0DescriptorEditor extends GeneratorDescriptorEditor {

	@Override
	public void addPages() {
		if (LicensedAccess.getWorkbenchPluggablePluginRole() != TSWorkbenchPluggablePluginRole.CREATE_EDIT
				&& LicensedAccess.getWorkbenchPluggablePluginRole() != TSWorkbenchPluggablePluginRole.DEPLOY_UNDEPLOY) {
			GeneratorDescriptorErrorPage errorPage = new GeneratorDescriptorErrorPage(
					this, "errorPage1", "Edit M0-Level Generator Error");
			try {
				addPage(errorPage);
			} catch (Exception e) {
				EclipsePlugin.log(e);
			}
		} else {
			int headerIndex = -1;
			try {
				M0OverviewPage overPage = new M0OverviewPage(this);
				headerIndex = addPage(overPage);
				addModelPage(overPage);

				PluginDescriptorPropertiesPage propertiesPage = new PluginDescriptorPropertiesPage(
						this);
				addPage(propertiesPage);
				addModelPage(propertiesPage);

				M0DescriptorRulesPage rulesPage = new M0DescriptorRulesPage(
						this);
				addPage(rulesPage);
				addModelPage(rulesPage);

				RuntimePage runtimePage = new RuntimePage(this);
				addPage(runtimePage);
				addModelPage(runtimePage);

				addSourcePage();
			} catch (PartInitException e) {
				EclipsePlugin.log(e);
			}
			setActivePage(headerIndex);
			updateTitle();
		}
	}

}