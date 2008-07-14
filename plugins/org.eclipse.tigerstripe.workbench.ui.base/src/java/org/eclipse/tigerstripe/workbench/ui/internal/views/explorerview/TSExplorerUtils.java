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

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
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

			IAbstractArtifact art = (IAbstractArtifact) jElem
					.getAdapter(IAbstractArtifact.class);

			// Now this may mean that the artifact hasn't been extracted yet,
			// but we're trying to display the proper icon in the explorer
			IResource res = jElem.getResource();
			if (art == null && res != null) {
				try {
					ITigerstripeModelProject project = (ITigerstripeModelProject) res
							.getProject().getAdapter(
									ITigerstripeModelProject.class);
					if (project != null) {
						IArtifactManagerSession mgr = project
								.getArtifactManagerSession();
						StringReader reader = new StringReader(jElem
								.getSource());
						art = mgr.extractArtifact(reader,
								new NullProgressMonitor());
					}
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
					return null;
				} catch (JavaModelException e) {
					EclipsePlugin.log(e);
					return null;
				}
			}
			return art;

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

		} else if (element instanceof IPackageFragment) {
			IPackageFragment res = (IPackageFragment) element;
			try {
				IAbstractTigerstripeProject aProject = TigerstripeCore
						.findProject(res.getCorrespondingResource()
								.getProject().getLocation().toFile().toURI());

				if (aProject instanceof ITigerstripeModelProject) {
					ITigerstripeModelProject project = (ITigerstripeModelProject) aProject;
					IArtifactManagerSession mgr = project
							.getArtifactManagerSession();
					// The FQN of the Artifact To get is the package name...
					IAbstractArtifact artifact = ((ArtifactManagerSessionImpl) mgr)
							.getArtifactManager()
							.getArtifactByFullyQualifiedName(
									res.getElementName(), false,
									new NullProgressMonitor());

					return artifact;
				} else
					return null;

			} catch (Exception e) {
				EclipsePlugin.log(e);
				return null;
			}
		} else if (element instanceof IFile) {

			IFile f = (IFile) element;
			IAbstractArtifact artifact = (IAbstractArtifact) f
					.getAdapter(IAbstractArtifact.class);

			return artifact;
			// try {
			// IAbstractTigerstripeProject aProject = TigerstripeCore
			// .findProject(f.getProject().getLocation()
			// .toFile().toURI());
			//
			// if (aProject instanceof ITigerstripeModelProject) {
			// ITigerstripeModelProject project = (ITigerstripeModelProject)
			// aProject;
			// IArtifactManagerSession mgr = project
			// .getArtifactManagerSession();
			//					
			// IPath location = f.getLocation();
			// if (location != null){
			// File file = location.toFile();
			//					
			//
			//					
			// artifact = ((ArtifactManagerSessionImpl) mgr)
			//.getArtifactManager().getArtifactByFilename(file.getAbsolutePath()
			// );
			// }
			// }
			// } catch (Exception e) {
			// EclipsePlugin.log(e);
			// return null;
			// }
			// //IAbstractArtifact artifact =
			// getArtifactFor(JavaCore.create((IFile) element));
			// return artifact;

		} else
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
