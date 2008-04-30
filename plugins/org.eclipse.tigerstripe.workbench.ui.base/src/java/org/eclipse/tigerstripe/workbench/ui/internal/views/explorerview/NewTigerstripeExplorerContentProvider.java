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
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JarEntryFile;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerContentProvider;
import org.eclipse.tigerstripe.workbench.internal.builder.TigerstripeProjectAuditor;
import org.eclipse.tigerstripe.workbench.internal.builder.natures.ProjectMigrationUtils;
import org.eclipse.tigerstripe.workbench.internal.builder.natures.TigerstripeM0GeneratorNature;
import org.eclipse.tigerstripe.workbench.internal.builder.natures.TigerstripePluginProjectNature;
import org.eclipse.tigerstripe.workbench.internal.builder.natures.TigerstripeProjectNature;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.LogicalExplorerNodeFactory;

public class NewTigerstripeExplorerContentProvider extends
		PackageExplorerContentProvider {

	public NewTigerstripeExplorerContentProvider(boolean provideMembers) {
		super(provideMembers);
	}

	@Override
	public Object[] getChildren(Object parentElement) {

		Object[] rawChildren = NO_CHILDREN;

		try {
			if (parentElement instanceof ICompilationUnit
					|| parentElement instanceof IClassFile) {
				IAbstractArtifact artifact = TSExplorerUtils
						.getArtifactFor(parentElement);
				if (artifact != null) {
					rawChildren = artifact.getChildren().toArray();
				}
			} else if (parentElement instanceof IJavaModel) {
				rawChildren = getTigerstripeProjects((IJavaModel) parentElement);
				// } else if (parentElement instanceof
				// org.eclipse.jdt.internal.ui.packageview.ClassPathContainer) {
				// rawChildren = NO_CHILDREN; // don't show the classpath jars.
				// // return
				// getContainerPackageFragmentRoots((ClassPathContainer)
				// // parentElement);
			} else if (parentElement instanceof JarPackageFragmentRoot) {
				// To enable proper rendering of Module components are
				// artifacts,
				// we need to post-process the result here
				Object[] tmpChildren = super.getChildren(parentElement);
				rawChildren = postProcessPackageFragmentRoot(
						(JarPackageFragmentRoot) parentElement, tmpChildren);
			} else {
				// delegate
				rawChildren = super.getChildren(parentElement);
			}
		} catch (JavaModelException jme) {
			rawChildren = super.getChildren(parentElement);
		}

		// Filter all children for logical nodes
		List<Object> filteredChildren = new ArrayList<Object>();
		for (Object object : rawChildren) {
			Object node = LogicalExplorerNodeFactory.getInstance().getNode(
					object);
			if (node != null) {
				filteredChildren.add(node);
			}
		}
		return filteredChildren.toArray(new Object[filteredChildren.size()]);
	}

	/**
	 * Post processing on JAR files to make them appear properly as TS modules
	 * when needed
	 * 
	 * @param packageRoot
	 * @param children
	 * @return
	 */
	private Object[] postProcessPackageFragmentRoot(
			IPackageFragmentRoot packageRoot, Object[] children) {
		ArrayList<Object> result = new ArrayList<Object>();

		for (Object obj : children) {
			if (obj instanceof JarEntryFile) {
				JarEntryFile file = (JarEntryFile) obj;
				if (file.getName().endsWith("ts-module.xml")) {
					continue; // ts-module shouldn't be displayed
				}
			} else if (obj instanceof IPackageFragment) {
				IPackageFragment fragment = (IPackageFragment) obj;
				if (fragment.getElementName().startsWith("META-INF")) {
					continue; // META-INF shouldn't be displayed
				}
			} else if (obj instanceof IStorage) {
				IStorage st = (IStorage) obj;
				if (st.getName().startsWith("META-INF")) {
					continue; // META-INF shouldn't be displayed
				}
			}
			result.add(obj);
		}

		return result.toArray();
	}

	/**
	 * Note: This method is for internal use only. Clients should not call this
	 * method.
	 */
	protected Object[] getTigerstripeProjects(IJavaModel jm)
			throws JavaModelException {
		List<IJavaProject> result = new ArrayList<IJavaProject>();
		IProject[] projects = EclipsePlugin.getWorkspace().getRoot()
				.getProjects();
		for (int i = 0; i < projects.length; i++) {
			try {

				// Since 1.2 the nature has changed name @see #295
				ProjectMigrationUtils.handleProjectMigration(projects[i]);

				if (TigerstripePluginProjectNature.hasNature(projects[i])) {
					result.add(jm.getJavaProject(projects[i].getName()));

					ProjectMigrationUtils
							.handlePluginProjectMigration(projects[i]);

					if (projects[i]
							.getAdapter(ITigerstripeGeneratorProject.class) != null) {

					}
				} else if (TigerstripeM0GeneratorNature.hasNature(projects[i])) {
					result.add(jm.getJavaProject(projects[i].getName()));

				} else if (TigerstripeProjectNature.hasNature(projects[i])) {
					result.add(jm.getJavaProject(projects[i].getName()));

					// At this point we build up the Artifact Manager
					// from the snapshot we have
					if (projects[i].getAdapter(ITigerstripeModelProject.class) != null) {

						// @since 0.4
						// For compatibility reasons, we check that a
						// TigerstripeProjectAuditor
						// is associated with the project. If not, just add it
						// silently.
						final IProject project = projects[i];
						if (!TigerstripeProjectAuditor
								.hasTigerstripeProjectAuditor(projects[i])) {
							TigerstripeProjectAuditor
									.addBuilderToProject(project);
							new Job("Tigerstripe Project Audit") {
								@Override
								protected IStatus run(IProgressMonitor monitor) {
									try {
										project
												.build(
														IncrementalProjectBuilder.FULL_BUILD,
														TigerstripeProjectAuditor.BUILDER_ID,
														null, monitor);
									} catch (CoreException e) {
										EclipsePlugin.log(e);
									}
									return org.eclipse.core.runtime.Status.OK_STATUS;
								}
							}.schedule();
						}
					}
				}
			} catch (CoreException e) {
				EclipsePlugin.log(e);
			}
		}
		return result.toArray();
	}

}
