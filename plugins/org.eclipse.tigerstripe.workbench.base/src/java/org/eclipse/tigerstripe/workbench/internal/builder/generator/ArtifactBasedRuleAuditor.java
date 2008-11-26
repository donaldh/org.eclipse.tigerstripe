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
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactRule;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactWrappedRule;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactFilter;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactWrapper;
import org.eclipse.tigerstripe.workbench.plugins.IRunnableRule;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateBasedRule;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;

public class ArtifactBasedRuleAuditor extends BasePluggableProjectAuditor {

	public ArtifactBasedRuleAuditor(ITigerstripeM1GeneratorProject pProject,
			IProject project) {
		super(pProject, project);
	}

	public void audit(IArtifactRule rule, IProgressMonitor monitor) {
		super.audit(rule, monitor);

		IArtifactRule aRule = (IArtifactRule) rule;

		if (aRule.isEnabled()) {
			checkArtifactType(aRule);
			checkArtifactFilterClass(aRule);
			if (aRule instanceof IArtifactWrappedRule){
				checkArtifactModelClass((IArtifactWrappedRule) aRule);
			}
			if (aRule instanceof IRunnableRule){
				checkRunnableClass((IRunnableRule) aRule);
			}
		}
	}

	protected void checkArtifactType(IArtifactRule aRule) {
		String artifactType = aRule.getArtifactType();
		if (artifactType == null || artifactType.length() == 0) {
			PluggablePluginProjectAuditor.reportError(
					"Artifact Type undefined in " + aRule.getLabel() + " '"
							+ aRule.getName() + "' in project '"
							+ getPProject().getName() + "'", projectDescriptor,
					222);
		}
	}

	protected void checkArtifactFilterClass(IArtifactRule aRule) {
		String artifactFilterClass = aRule.getArtifactFilterClass();
		if (artifactFilterClass != null
				&& artifactFilterClass.trim().length() != 0) {
			IJavaProject jProject = JavaCore.create(getProject());
			try {
				IType type = jProject.findType(artifactFilterClass);
				if (type == null) {
					PluggablePluginProjectAuditor.reportError(
							"Classname undefined for artifact filter ("
									+ artifactFilterClass + ") in "
									+ aRule.getLabel() + " '" + aRule.getName()
									+ "' in project '"
									+ getPProject().getName() + "'",
							projectDescriptor, 222);
				} else {
					// The classname needs to be a class that implements
					// IArtifactFilter
					if (!type.isClass()) {
						PluggablePluginProjectAuditor.reportError(
								"The artifact filter (" + artifactFilterClass
										+ ") in " + aRule.getLabel() + " '"
										+ aRule.getName()
										+ "' is not a Java class in project '"
										+ getPProject().getName() + "'",
								projectDescriptor, 222);
					} else {
						String[] intfs = getImplementatedInterfaces(type,
								jProject);
						boolean found = false;
						for (String intf : intfs) {
							// FIXME: this is currently testing only the
							// non-qualified name
							// because this is what is returned by
							// getSuperIntefaceNames().
							// This should be changed to a test on the FQN.
							if (IArtifactFilter.class.getName().endsWith(intf)) {
								found = true;
							}
						}
						if (!found) {
							PluggablePluginProjectAuditor.reportError(
									"The artifact filter ("
											+ artifactFilterClass + ") in "
											+ aRule.getLabel() + " '"
											+ aRule.getName()
											+ "' must implement '"
											+ IArtifactFilter.class.getName()
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

	protected void checkArtifactModelClass(IArtifactWrappedRule aRule) {
		String artifactModelClass = aRule.getModelClass();
		if (artifactModelClass != null
				&& artifactModelClass.trim().length() != 0) {
			IJavaProject jProject = JavaCore.create(getProject());
			try {
				IType type = jProject.findType(artifactModelClass);
				if (type == null) {
					PluggablePluginProjectAuditor.reportError(
							"Classname undefined for model ("
									+ artifactModelClass + ") in "
									+ aRule.getLabel() + " '" + aRule.getName()
									+ "' in project '"
									+ getPProject().getName() + "'",
							projectDescriptor, 222);
				} else {
					// The classname needs to be a class that implements
					// IArtifactWrapper
					if (!type.isClass()) {
						PluggablePluginProjectAuditor.reportError("The model ("
								+ artifactModelClass + ") in "
								+ aRule.getLabel() + " '" + aRule.getName()
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
							if (IArtifactWrapper.class.getName().endsWith(intf)) {
								found = true;
							}
						}
						if (!found) {
							PluggablePluginProjectAuditor.reportError(
									"The artifact wrapper ("
											+ artifactModelClass + ") in "
											+ aRule.getLabel() + " '"
											+ aRule.getName()
											+ "' must implement '"
											+ IArtifactWrapper.class.getName()
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
}
