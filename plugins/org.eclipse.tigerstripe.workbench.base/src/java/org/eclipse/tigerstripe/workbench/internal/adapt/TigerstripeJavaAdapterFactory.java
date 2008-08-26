/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.internal.adapt;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.model.ArtifactManager;
import org.eclipse.tigerstripe.workbench.internal.core.project.Dependency;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.project.IAbstractTigerstripeProject;
import org.eclipse.tigerstripe.workbench.project.IDependency;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM0GeneratorProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

/**
 * Overall Tigerstripe Adapter factory
 * 
 * @author erdillon
 * 
 */
public class TigerstripeJavaAdapterFactory implements IAdapterFactory {

	@SuppressWarnings("unchecked")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof IClassFile) {
			IClassFile cFile = (IClassFile) adaptableObject;
			try {
				if (cFile.getCorrespondingResource() == null) {
					// This means we're looking at a IClassFile inside a module
					IDependency dep = getDependencyFor(cFile);
					if (dep != null) {
						ArtifactManager mgr = ((Dependency) dep)
								.getArtifactManager(new NullProgressMonitor()); // FIXME
						String fqn = getFQNfor(cFile);
						IAbstractArtifact art = mgr
								.getArtifactByFullyQualifiedName(fqn, false,
										new NullProgressMonitor());
						if (art != null)
							return art.getAdapter(adapterType);
					}
					return null;
				}
			} catch (JavaModelException e) {
				BasePlugin.log(e);
				return null;
			}
		} else if (adaptableObject instanceof IJavaElement) {
			IJavaElement element = (IJavaElement) adaptableObject;
			if (element.getResource() != null)
				return element.getResource().getAdapter(adapterType);
		} else if (adaptableObject instanceof IJavaProject) {
			IJavaProject jProject = (IJavaProject) adaptableObject;
			if (jProject.getResource() != null)
				return jProject.getResource().getAdapter(adapterType);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public Class[] getAdapterList() {
		return new Class[] { ITigerstripeModelProject.class,
				ITigerstripeGeneratorProject.class,
				ITigerstripeM0GeneratorProject.class,
				ITigerstripeM1GeneratorProject.class,
				IAbstractTigerstripeProject.class, IModelComponent.class };
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
						BasePlugin.log(e);
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

}
