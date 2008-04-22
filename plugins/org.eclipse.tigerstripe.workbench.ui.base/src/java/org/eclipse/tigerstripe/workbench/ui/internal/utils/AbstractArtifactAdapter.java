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
package org.eclipse.tigerstripe.workbench.ui.internal.utils;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TSExplorerUtils;

/**
 * Adapts an object to a corresponding Abstract artifact if possible
 * 
 * @author Eric Dillon
 * 
 */
public class AbstractArtifactAdapter {

	/**
	 * Adapts to an Abstract artifact within the context of the given project.
	 * If the corresponding artifact is not found with this context null is
	 * return (even if the artifact would exist outside of the given project)
	 * 
	 * @param obj
	 * @param tsProject
	 * @return
	 */
	public static IAbstractArtifact adaptWithin(Object obj,
			ITigerstripeModelProject tsProject) {

		if (tsProject == null)
			return null;

		if (obj instanceof IJavaElement) {
			IJavaElement jElement = (IJavaElement) obj;
			IJavaProject jProject = jElement.getJavaProject();

			if (jElement instanceof ICompilationUnit) {

				if (EclipsePlugin.getIJavaProject(tsProject) != null) {
					IProject diagProj = EclipsePlugin
							.getIJavaProject(tsProject).getProject();

					try {
						boolean found = false;
						IProject[] refProjects = diagProj
								.getReferencedProjects();

						if (diagProj.equals(jProject.getProject())) {
							found = true;
						} else {
							for (IProject prj : refProjects) {
								if (prj.equals(jProject.getProject())) {
									found = true;
								}
							}
						}

						if (!found)
							return null;
					} catch (CoreException e) {
						EclipsePlugin.log(e);
					}
				}

				ICompilationUnit unit = (ICompilationUnit) jElement;
				try {
					IArtifactManagerSession session = tsProject
							.getArtifactManagerSession();
					IPath path = unit.getPrimary().getPath()
							.removeFileExtension();

					IPackageFragmentRoot[] roots = jProject
							.getPackageFragmentRoots();
					for (IPackageFragmentRoot root : roots) {
						IPath rootPath = root.getPath();
						if (rootPath.isPrefixOf(path)) {
							int segments = rootPath.segmentCount();
							// replaced windows-specific hack...
							String fqn = path.removeFirstSegments(segments)
									.toOSString().replace(File.separatorChar,
											'.');
							return session.getArtifactByFullyQualifiedName(fqn,
									true);
						}
					}
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				} catch (JavaModelException e) {
					EclipsePlugin.log(e);
				}
			} else if (jElement instanceof IClassFile) {
				IAbstractArtifact artifact = TSExplorerUtils
						.getArtifactFor(jElement);
				// At that stage we need to make sure that the artifact that was
				// returned
				// is actually in the classpath of the project in scope
				IAbstractTigerstripeProject proj = EclipsePlugin
						.getITigerstripeProjectFor(jElement.getJavaProject()
								.getProject());
				if (proj != null && proj.equals(tsProject))
					return artifact;
			}
		}
		return null;
	}

	/**
	 * Adapts to an Artifact no matter the corresponding artifact can be found
	 * 
	 * @param obj
	 * @return
	 */
	public static IAbstractArtifact adapt(Object obj) {
		if (obj instanceof IJavaElement) {
			IJavaElement jElement = (IJavaElement) obj;
			IJavaProject jProject = jElement.getJavaProject();

			IAbstractTigerstripeProject aProject = EclipsePlugin
					.getITigerstripeProjectFor(jProject.getProject());

			if (aProject instanceof ITigerstripeModelProject
					&& (jElement instanceof ICompilationUnit || jElement instanceof IClassFile))
				return adaptWithin(obj, (ITigerstripeModelProject) aProject);
		}
		return null;
	}
}
