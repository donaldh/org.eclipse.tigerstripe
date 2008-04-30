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
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactBasedTemplateRule;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactFilter;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactModel;
import org.eclipse.tigerstripe.workbench.plugins.ITemplateBasedRule;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;

public class ArtifactBasedRuleAuditor extends BasePluggableProjectAuditor {

	public ArtifactBasedRuleAuditor(ITigerstripeM1GeneratorProject pProject,
			IProject project) {
		super(pProject, project);
	}

	public void audit(ITemplateBasedRule rule, IProgressMonitor monitor) {
		super.audit(rule, monitor);

		IArtifactBasedTemplateRule aRule = (IArtifactBasedTemplateRule) rule;

		if (aRule.isEnabled()) {
			checkArtifactType(aRule);
			checkArtifactFilterClass(aRule);
			checkArtifactModelClass(aRule);
		}
	}

	protected void checkArtifactType(IArtifactBasedTemplateRule aRule) {
		String artifactType = aRule.getArtifactType();
		if (artifactType == null || artifactType.length() == 0) {
			PluggablePluginProjectAuditor.reportError(
					"Artifact Type undefined in " + aRule.getLabel() + " '"
							+ aRule.getName() + "' in project '"
							+ getPProject().getProjectLabel() + "'",
					projectDescriptor, 222);
		}
	}

	protected void checkArtifactFilterClass(IArtifactBasedTemplateRule aRule) {
		String artifactFilterClass = aRule.getArtifactFilterClass();
		if (artifactFilterClass != null
				&& artifactFilterClass.trim().length() != 0) {
			IJavaProject jProject = JavaCore.create(getProject());
			try {
				IType type = jProject.findType(artifactFilterClass);
				if (type == null) {
					PluggablePluginProjectAuditor.reportError(
							"Classname undefined for artifact filter ("
									+ artifactFilterClass
									+ ") in "
									+ aRule.getLabel()
									+ " '"
									+ aRule.getName()
									+ "' in project '"
									+ getPProject().getProjectDetails()
											.getName() + "'",
							projectDescriptor, 222);
				} else {
					// The classname needs to be a class that implements
					// IArtifactFilter
					if (!type.isClass()) {
						PluggablePluginProjectAuditor.reportError(
								"The artifact filter ("
										+ artifactFilterClass
										+ ") in "
										+ aRule.getLabel()
										+ " '"
										+ aRule.getName()
										+ "' is not a Java class in project '"
										+ getPProject().getProjectDetails()
												.getName() + "'",
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
											+ artifactFilterClass
											+ ") in "
											+ aRule.getLabel()
											+ " '"
											+ aRule.getName()
											+ "' must implement '"
											+ IArtifactFilter.class.getName()
											+ "' in project '"
											+ getPProject().getProjectDetails()
													.getName() + "'",
									projectDescriptor, 222);
						}
					}
				}
			} catch (JavaModelException e) {
				// ignore ?
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
			}
		}

	}

	protected void checkArtifactModelClass(IArtifactBasedTemplateRule aRule) {
		String artifactModelClass = aRule.getModelClass();
		if (artifactModelClass != null
				&& artifactModelClass.trim().length() != 0) {
			IJavaProject jProject = JavaCore.create(getProject());
			try {
				IType type = jProject.findType(artifactModelClass);
				if (type == null) {
					PluggablePluginProjectAuditor.reportError(
							"Classname undefined for model ("
									+ artifactModelClass
									+ ") in "
									+ aRule.getLabel()
									+ " '"
									+ aRule.getName()
									+ "' in project '"
									+ getPProject().getProjectDetails()
											.getName() + "'",
							projectDescriptor, 222);
				} else {
					// The classname needs to be a class that implements
					// IArtifactModel
					if (!type.isClass()) {
						PluggablePluginProjectAuditor.reportError("The model ("
								+ artifactModelClass + ") in "
								+ aRule.getLabel() + " '" + aRule.getName()
								+ "' is not a Java class in project '"
								+ getPProject().getProjectLabel() + "'",
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
							if (IArtifactModel.class.getName().endsWith(intf)) {
								found = true;
							}
						}
						if (!found) {
							PluggablePluginProjectAuditor.reportError(
									"The artifact model ("
											+ artifactModelClass
											+ ") in "
											+ aRule.getLabel()
											+ " '"
											+ aRule.getName()
											+ "' must implement '"
											+ IArtifactModel.class.getName()
											+ "' in project '"
											+ getPProject().getProjectDetails()
													.getName() + "'",
									projectDescriptor, 222);
						}
					}
				}
			} catch (JavaModelException e) {
				// ignore ?
			} catch (TigerstripeException e) {
				BasePlugin.log(e);
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
