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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.VelocityContextDefinition;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateBasedRule;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;

public abstract class BasePluggableProjectAuditor {

	private ITigerstripeM1GeneratorProject pProject;
	private IProject project;
	protected IResource projectDescriptor;

	protected ITigerstripeM1GeneratorProject getPProject() {
		return pProject;
	}

	protected IProject getProject() {
		return project;
	}

	public BasePluggableProjectAuditor(ITigerstripeM1GeneratorProject pProject,
			IProject project) {
		this.project = project;
		this.pProject = pProject;

		this.projectDescriptor = project
				.findMember(ITigerstripeConstants.PLUGIN_DESCRIPTOR);
		if (this.projectDescriptor == null || !this.projectDescriptor.exists()) {
			this.projectDescriptor = project;
		}
	}
	
	public void audit(IRule rule, IProgressMonitor monitor) {

		if (!rule.isEnabled())
			return;

		try {
			// check name
			String name = rule.getName();
			if (name == null || name.trim().length() == 0) {
				PluggablePluginProjectAuditor.reportError(
						"A "+rule.getLabel()+" with no name is defined in project '"
								+ getPProject().getProjectLabel()
								+ "'", projectDescriptor, 222);
			}

			// Check description
			String description = rule.getDescription();
			if (description == null || description.trim().length() == 0) {
				PluggablePluginProjectAuditor.reportInfo(
						"No description for "+rule.getLabel()+" '" + name
								+ "' in project '"
								+ getPProject().getProjectLabel()
								+ "'", projectDescriptor, 222);
			}

			if (rule instanceof ITemplateBasedRule) {
				// check template is defined and found
				String template = ((ITemplateBasedRule) rule).getTemplate();
				if (template == null || template.trim().length() == 0) {
					PluggablePluginProjectAuditor.reportError(
							"No Template specified for "+rule.getLabel()+" '"
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
								"Template not found for "+rule.getLabel()+" '"
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
							"No specified output filename for "+rule.getLabel()+" '"
									+ name
									+ "' in project '"
									+ getPProject().getProjectDetails()
											.getName() + "'",
							projectDescriptor, 222);
				}

				checkVelocityContextDefinitions(((ITemplateBasedRule) rule));
			} 
		} catch (TigerstripeException e) {
			BasePlugin.log(e);
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
							"Invalid Velocity Context Entry (no name) in "+rule.getLabel()+" '"
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
										+ "' in "+rule.getLabel()+" '"
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
			BasePlugin.log(e);
		}
	}
}
