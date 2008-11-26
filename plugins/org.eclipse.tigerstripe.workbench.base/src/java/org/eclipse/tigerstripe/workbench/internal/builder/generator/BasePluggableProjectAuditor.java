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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.VelocityContextDefinition;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactWrapper;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.eclipse.tigerstripe.workbench.plugins.IRunnableRule;
import org.eclipse.tigerstripe.workbench.plugins.IRunnableWrapper;
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

		// check name
		String name = rule.getName();
		if (name == null || name.trim().length() == 0) {
			PluggablePluginProjectAuditor.reportError("A " + rule.getLabel()
					+ " with no name is defined in project '"
					+ getPProject().getName() + "'", projectDescriptor, 222);
		}

		// Check description
		String description = rule.getDescription();
		if (description == null || description.trim().length() == 0) {
			PluggablePluginProjectAuditor.reportInfo("No description for "
					+ rule.getLabel() + " '" + name + "' in project '"
					+ getPProject().getName() + "'", projectDescriptor, 222);
		}

		if (rule instanceof ITemplateBasedRule) {
			// check template is defined and found
			String template = ((ITemplateBasedRule) rule).getTemplate();
			if (template == null || template.trim().length() == 0) {
				PluggablePluginProjectAuditor.reportError(
						"No Template specified for " + rule.getLabel() + " '"
								+ name + "' in project '"
								+ getPProject().getName() + "'",
						projectDescriptor, 222);
			} else {
				IProject project = getProject();
				IResource res = project.findMember(template);
				if (res == null) {
					PluggablePluginProjectAuditor.reportError(
							"Template not found for " + rule.getLabel() + " '"
									+ name + "' in project '"
									+ getPProject().getName() + "'",
							projectDescriptor, 222);
				}
			}

			// Check an output file is defined
			String output = ((ITemplateBasedRule) rule).getOutputFile();
			if (output == null || output.trim().length() == 0) {
				PluggablePluginProjectAuditor.reportError(
						"No specified output filename for " + rule.getLabel()
								+ " '" + name + "' in project '"
								+ getPProject().getName() + "'",
						projectDescriptor, 222);
			}

			checkVelocityContextDefinitions(((ITemplateBasedRule) rule));
		}
	}

	protected void checkVelocityContextDefinitions(ITemplateBasedRule rule) {
		VelocityContextDefinition[] defs = rule.getVelocityContextDefinitions();
		for (VelocityContextDefinition def : defs) {
			String entry = def.getName();
			if (entry == null || entry.trim().length() == 0) {
				PluggablePluginProjectAuditor.reportError(
						"Invalid Velocity Context Entry (no name) in "
								+ rule.getLabel() + " '" + rule.getName()
								+ "' in project '" + getPProject().getName()
								+ "'", projectDescriptor, 222);
			}

			String clazz = def.getClassname();

			IJavaProject jProject = JavaCore.create(getProject());
			try {
				IType type = jProject.findType(clazz);
				if (type == null) {
					PluggablePluginProjectAuditor.reportError(
							"Classname undefined for Velocity Context Entry '"
									+ entry + "' in " + rule.getLabel() + " '"
									+ rule.getName() + "' in project '"
									+ getPProject().getName() + "'",
							projectDescriptor, 222);
				}
			} catch (JavaModelException e) {
				// ignore?
			}
		}
	}
	
	protected void checkRunnableClass(IRunnableRule rule){
		String runnableClassName = rule.getRunnableClassName();
		if (runnableClassName != null
				&& runnableClassName.trim().length() != 0) {
			IJavaProject jProject = JavaCore.create(getProject());
			try {
				IType type = jProject.findType(runnableClassName);
				if (type == null) {
					PluggablePluginProjectAuditor.reportError(
							"Classname undefined for runnable ("
									+ runnableClassName + ") in "
									+ rule.getLabel() + " '" + rule.getName()
									+ "' in project '"
									+ getPProject().getName() + "'",
							projectDescriptor, 222);
				} else {
					// The classname needs to be a class that implements
					// IRunnable
					if (!type.isClass()) {
						PluggablePluginProjectAuditor.reportError("The runnable class ("
								+ runnableClassName + ") in "
								+ rule.getLabel() + " '" + rule.getName()
								+ "' is not a Java class in project '"
								+ getPProject().getName() + "'",
								projectDescriptor, 222);
					} else {

						// Let's build a list of all implemented interfaces
						String[] intfs = getImplementatedInterfaces(type,
								jProject);

						boolean found = false;
						for (String intf : intfs) {
							// FIXME: this is currently testing only the
							// non-qualified name
							// because this is what is returned by
							// getSuperIntefaceNames().
							// This should be changed to a test on the FQN.
							if (IRunnableWrapper.class.getName().endsWith(intf)) {
								found = true;
							}
						}
						if (!found) {
							PluggablePluginProjectAuditor.reportError(
									"The runnable class ("
											+ runnableClassName + ") in "
											+ rule.getLabel() + " '"
											+ rule.getName()
											+ "' must implement '"
											+ IRunnableWrapper.class.getName()
											+ "' in project '"
											+ getPProject().getName() + "'",
									projectDescriptor, 222);
						}
					}
				}
			} catch (JavaModelException e) {
				// ignore ?
			}
		}

	}

	/**
	 * Builds a list of all implemented interfaces recursively
	 * 
	 * @param type
	 * @return
	 */
	protected String[] getImplementatedInterfaces(IType type,
			IJavaProject jProject) {
		List<String> result = new ArrayList<String>();
	
		if (type == null)
			return result.toArray(new String[result.size()]);
		try {
			ITypeHierarchy typeHierarchy = type
					.newSupertypeHierarchy(new NullProgressMonitor());
			IType[] superInterfaces = typeHierarchy.getAllInterfaces();
			for (IType intf : superInterfaces) {
				result.add(intf.getFullyQualifiedName());
			}
		} catch (JavaModelException e) {
			// ignore
		}
	
		return result.toArray(new String[result.size()]);
	}
}
