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
package org.eclipse.tigerstripe.workbench.ui.eclipse.builder.pluggable;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.VelocityContextDefinition;
import org.eclipse.tigerstripe.workbench.plugins.ICopyRule;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateBasedRule;
import org.eclipse.tigerstripe.workbench.project.ITigerstripePluginProject;

public class GlobalRuleAuditor extends BasePluggableProjectAuditor {

	public GlobalRuleAuditor(ITigerstripePluginProject pProject, IProject project) {
		super(pProject, project);
	}

	public void audit(IRule rule, IProgressMonitor monitor) {

		if (!rule.isEnabled())
			return;

		try {
			// check name
			String name = rule.getName();
			if (name == null || name.trim().length() == 0) {
				PluggablePluginProjectAuditor.reportError(
						"A global rule with no name is defined in project '"
								+ getPProject().getProjectLabel()
								+ "'", projectDescriptor, 222);
			}

			// Check description
			String description = rule.getDescription();
			if (description == null || description.trim().length() == 0) {
				PluggablePluginProjectAuditor.reportInfo(
						"No description for global rule '" + name
								+ "' in project '"
								+ getPProject().getProjectLabel()
								+ "'", projectDescriptor, 222);
			}

			if (rule instanceof ITemplateBasedRule) {
				// check template is defined and found
				String template = ((ITemplateBasedRule) rule).getTemplate();
				if (template == null || template.trim().length() == 0) {
					PluggablePluginProjectAuditor.reportError(
							"No Template specified for global rule '"
									+ name
									+ "' in project '"
									+ getPProject().getProjectDetails()
											.getName() + "'",
							projectDescriptor, 222);
				} else {
					IProject project = getProject();
					IResource res = project.findMember(template);
					if (res == null) {
						PluggablePluginProjectAuditor.reportError(
								"Template not found for global rule '"
										+ name
										+ "' in project '"
										+ getPProject().getProjectDetails()
												.getName() + "'",
								projectDescriptor, 222);
					}
				}

				// Check an output file is defined
				String output = ((ITemplateBasedRule) rule).getOutputFile();
				if (output == null || output.trim().length() == 0) {
					PluggablePluginProjectAuditor.reportError(
							"No specified output filename for global rule '"
									+ name
									+ "' in project '"
									+ getPProject().getProjectDetails()
											.getName() + "'",
							projectDescriptor, 222);
				}

				checkVelocityContextDefinitions(((ITemplateBasedRule) rule));
			} else if (rule instanceof ICopyRule) {
				ICopyRule cRule = (ICopyRule) rule;
				if (cRule.getFilesetMatch() == null
						|| cRule.getFilesetMatch().length() == 0) {
					PluggablePluginProjectAuditor.reportError(
							"Invalid source for Copy Rule '"
									+ name
									+ "' in project '"
									+ getPProject().getProjectDetails()
											.getName() + "'",
							projectDescriptor, 222);
				}

				if (cRule.getToDirectory() == null
						|| cRule.getToDirectory().length() == 0) {
					PluggablePluginProjectAuditor.reportError(
							"Invalid target directory for Copy Rule '"
									+ name
									+ "' in project '"
									+ getPProject().getProjectDetails()
											.getName() + "'",
							projectDescriptor, 222);
				}
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	protected void checkVelocityContextDefinitions(ITemplateBasedRule rule) {
		try {
			VelocityContextDefinition[] defs = rule
					.getVelocityContextDefinitions();
			for (VelocityContextDefinition def : defs) {
				String entry = def.getName();
				if (entry == null || entry.trim().length() == 0) {
					PluggablePluginProjectAuditor.reportError(
							"Invalid Velocity Context Entry (no name) in global rule '"
									+ rule.getName()
									+ "' in project '"
									+ getPProject().getProjectDetails()
											.getName() + "'",
							projectDescriptor, 222);
				}

				String clazz = def.getClassname();

				IJavaProject jProject = JavaCore.create(getProject());
				try {
					IType type = jProject.findType(clazz);
					if (type == null) {
						PluggablePluginProjectAuditor.reportError(
								"Classname undefined for Velocity Context Entry '"
										+ entry
										+ "' in global rule '"
										+ rule.getName()
										+ "' in project '"
										+ getPProject().getProjectDetails()
												.getName() + "'",
								projectDescriptor, 222);
					}
				} catch (JavaModelException e) {
					// ignore?
				}
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}
}
