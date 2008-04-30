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
package org.eclipse.tigerstripe.workbench.internal.builder.generator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.plugins.ICopyRule;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;

public class GlobalRuleAuditor extends BasePluggableProjectAuditor {

	public GlobalRuleAuditor(ITigerstripeM1GeneratorProject pProject, IProject project) {
		super(pProject, project);
	}

	public void audit(IRule rule, IProgressMonitor monitor) {
		super.audit(rule, monitor);
		
		if (!rule.isEnabled())
			return;

		try {
			String name = rule.getName();
			if (rule instanceof ICopyRule) {
				ICopyRule cRule = (ICopyRule) rule;
				if (cRule.getFilesetMatch() == null
						|| cRule.getFilesetMatch().length() == 0) {
					PluggablePluginProjectAuditor.reportError(
							"Invalid source for "+rule.getLabel()+" '"
									+ name
									+ "' in project '"
									+ getPProject().getProjectDetails()
											.getName() + "'",
							projectDescriptor, 222);
				}

				if (cRule.getToDirectory() == null
						|| cRule.getToDirectory().length() == 0) {
					PluggablePluginProjectAuditor.reportError(
							"Invalid target directory for "+rule.getLabel()+" '"
									+ name
									+ "' in project '"
									+ getPProject().getProjectDetails()
											.getName() + "'",
							projectDescriptor, 222);
				}
			}
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
		}
	}


}
