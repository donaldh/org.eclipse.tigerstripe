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
package org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.tigerstripe.api.API;
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.TigerstripeLicenseException;
import org.eclipse.tigerstripe.api.impl.ArtifactManagerSessionImpl;
import org.eclipse.tigerstripe.api.model.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.api.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.api.project.IDependency;
import org.eclipse.tigerstripe.api.project.IProjectSession;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.model.AbstractArtifact;
import org.eclipse.tigerstripe.core.model.ArtifactManager;
import org.eclipse.tigerstripe.core.project.Dependency;
import org.eclipse.tigerstripe.core.util.TigerstripeNullProgressMonitor;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;

public class TSExplorerUtils {

	public static IResource getIResourceForArtifact(IAbstractArtifact artifact)
			throws TigerstripeException {
		AbstractArtifact art = (AbstractArtifact) artifact;
		String artifactPath = art.getArtifactPath();

		if (artifactPath == null)
			throw new TigerstripeException("Unknown path for "
					+ artifact.getFullyQualifiedName()); // this happens for
		// artifacts in
		// modules.

		IJavaProject jProject = EclipsePlugin.getIJavaProject(artifact
				.getIProject());
		if (jProject == null)
			// This will happen when considering artifact from Phantom Project
			throw new TigerstripeException("Unknown path for "
					+ artifact.getFullyQualifiedName());
		IProject iProject = jProject.getProject();

		return iProject.findMember(artifactPath);
	}

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
					IProjectSession session = API.getDefaultProjectSession();
					IAbstractTigerstripeProject aProject = session
							.makeTigerstripeProject(res.getProject()
									.getLocation().toFile().toURI(), null);

					if (aProject instanceof ITigerstripeProject) {
						ITigerstripeProject project = (ITigerstripeProject) aProject;
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
									new TigerstripeNullProgressMonitor());
						}
						return artifact;
					} else
						return null;
				} catch (TigerstripeLicenseException e) {
					EclipsePlugin.log(e);
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
						.getArtifactManager(new TigerstripeNullProgressMonitor()); // FIXME
				String fqn = getFQNfor(classFile);
				return mgr.getArtifactByFullyQualifiedName(fqn, false,
						new TigerstripeNullProgressMonitor());
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
				IAbstractTigerstripeProject atsProject = EclipsePlugin
						.getITigerstripeProjectFor(project);
				if (atsProject instanceof ITigerstripeProject) {
					ITigerstripeProject tsProject = (ITigerstripeProject) atsProject;
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

	public static IAbstractTigerstripeProject getProjectHandleFor(Object element) {

		IProject project = null;

		if (element instanceof IProject) {
			project = (IProject) element;
			if (!project.exists() || !project.isOpen())
				return null;
		} else if (element instanceof IAdaptable) {
			IFile file = (IFile) ((IAdaptable) element).getAdapter(IFile.class);
			if (file != null) {
				project = file.getProject();
				if (!project.exists() || !project.isOpen())
					return null;
			}
		}

		if (project != null) {
			try {
				IProjectSession session = API.getDefaultProjectSession();
				IAbstractTigerstripeProject tsProject = session
						.makeTigerstripeProject(project.getLocation().toFile()
								.toURI(), null);

				return tsProject;
			} catch (TigerstripeLicenseException e) {
				EclipsePlugin.log(e);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
		return null;
	}
}
