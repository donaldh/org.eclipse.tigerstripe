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

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.workbench.internal.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.project.Dependency;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;

public class TSExplorerUtils {

	public static IAbstractArtifact getArtifactModelFor(Object element) {
		IAbstractArtifact art = getArtifactFor(element);
		if (art != null)
			return ((AbstractArtifact) art).getModel();
		return null;
	}

	/**
	 * Returns all Artifacts contained in the given package fragment
	 * 
	 * @param fragment
	 * @return
	 */
	public static List<IAbstractArtifact> findAllContainedArtifacts(
			IPackageFragment fragment) {
		List<IAbstractArtifact> result = new ArrayList<IAbstractArtifact>();

		try {
			for (IJavaElement element : fragment.getChildren()) {
				if (element instanceof ICompilationUnit) {
					IAbstractArtifact art = getArtifactFor(element);
					if (art != null)
						result.add(art);
				} else if (element instanceof IPackageFragment) {
					result
							.addAll(findAllContainedArtifacts((IPackageFragment) element));
				}
			}
		} catch (JavaModelException e) {
			EclipsePlugin.log(e);
		}
		return result;
	}

	// Util methods
	public static IAbstractArtifact getArtifactFor(Object element) {
		if (element instanceof ICompilationUnit) {
			ICompilationUnit jElem = (ICompilationUnit) element;
			IResource res = jElem.getResource();
			if (res != null) {
				try {
					IAbstractTigerstripeProject aProject = TigerstripeCore
							.findProject(res.getProject().getLocation()
									.toFile().toURI());

					if (aProject instanceof ITigerstripeModelProject) {
						ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
						IArtifactManagerSession mgr = project
								.getArtifactManagerSession();

						IAbstractArtifact artifact = ((ArtifactManagerSessionImpl) mgr)
								.getArtifactManager().getArtifactByFilename(
										jElem.getCorrespondingResource()
												.getLocation().toOSString());

						if (artifact == null) {
							StringReader reader = new StringReader(jElem
									.getSource());
							artifact = mgr.extractArtifact(reader,
									new NullProgressMonitor());
						}
						return artifact;
					} else
						return null;

				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
					return null;
				} catch (JavaModelException e) {
					EclipsePlugin.log(e);
					return null;
				}
			} else
				return null;
		} else if (element instanceof IClassFile) {
			IClassFile classFile = (IClassFile) element;
			IDependency dep = getDependencyFor(classFile);
			if (dep != null) {
				ArtifactManager mgr = ((Dependency) dep)
						.getArtifactManager(new NullProgressMonitor()); // FIXME
				String fqn = getFQNfor(classFile);
				return mgr.getArtifactByFullyQualifiedName(fqn, false,
						new NullProgressMonitor());
			} else
				return null;
		} else if (element instanceof IFile)
			return getArtifactFor(JavaCore.create((IFile) element));
		else
			return null;
	}

	public static String getFQNfor(IClassFile classFile) {
		String name = classFile.getElementName().replaceFirst("\\.class", "");
		if (classFile.getParent() instanceof IPackageFragment) {
			IPackageFragment fragment = (IPackageFragment) classFile
					.getParent();
			String packageName = fragment.getElementName();
			if (packageName != null && packageName.length() != 0)
				return packageName + "." + name;
			else
				return name;
		}
		return name;
	}

	public static IDependency getDependencyFor(IClassFile classFile) {
		IPackageFragmentRoot rootJar = getIPackageFragmentRootFor(classFile);
		if (rootJar != null) {
			IJavaProject jProject = rootJar.getJavaProject();
			if (jProject != null) {
				IProject project = jProject.getProject();
				IAbstractTigerstripeProject atsProject = (IAbstractTigerstripeProject) project
						.getAdapter(IAbstractTigerstripeProject.class);
				if (atsProject instanceof ITigerstripeModelProject) {
					ITigerstripeModelProject tsProject = (ITigerstripeModelProject) atsProject;
					try {
						for (IDependency dep : tsProject.getDependencies()) {
							if (dep.getPath().equals(
									rootJar.getPath().lastSegment()))
								return dep;
						}
					} catch (TigerstripeException e) {
						EclipsePlugin.log(e);
					}
				}
			}
		}
		return null;
	}

	private static IPackageFragmentRoot getIPackageFragmentRootFor(
			IJavaElement classFile) {
		if (classFile.getParent() == null)
			return null;
		else if (classFile.getParent() instanceof IPackageFragmentRoot)
			return (IPackageFragmentRoot) classFile.getParent();
		else
			return getIPackageFragmentRootFor(classFile.getParent());
	}

}
