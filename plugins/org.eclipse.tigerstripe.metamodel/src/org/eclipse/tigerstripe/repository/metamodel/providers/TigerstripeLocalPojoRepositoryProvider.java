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
package org.eclipse.tigerstripe.repository.metamodel.providers;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.tigerstripe.repository.internal.Activator;
import org.eclipse.tigerstripe.repository.manager.IModelRepository;
import org.eclipse.tigerstripe.repository.manager.IModelRepositoryProvider;
import org.eclipse.tigerstripe.repository.manager.ModelCoreException;
import org.eclipse.tigerstripe.repository.metamodel.pojo.MultiFileArtifactRepository;

public class TigerstripeLocalPojoRepositoryProvider implements
		IModelRepositoryProvider {

	public IModelRepository create(URI uri) throws ModelCoreException {
		return new MultiFileArtifactRepository(uri);
	}

	@SuppressWarnings("unchecked")
	public <T extends IModelRepository> Class<T>[] getSupportedRepositories() {
		return new Class[] { MultiFileArtifactRepository.class };
	}

	/*
	 * Basically accepting directories that are IPackageFragmentRoot
	 * 
	 * @see org.eclipse.tigerstripe.repository.manager.IModelRepositoryProvider#understandsURI(org.eclipse.emf.common.util.URI)
	 */
	public boolean understandsURI(URI uri) {
		if (uri.isPlatform()) {
			String plt = uri.toPlatformString(true);
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IResource src = root.findMember(plt);
			if (src != null && src.getProject().isOpen() && src.exists()) {
				IJavaProject jProject = JavaCore.create(src.getProject());
				if (jProject != null) {
					try {
						IPackageFragmentRoot[] roots = jProject
								.getAllPackageFragmentRoots();
						for (IPackageFragmentRoot pRoot : roots) {
							if (src.equals(pRoot.getResource())) {
								return true;
							}
						}
					} catch (JavaModelException e) {
						Activator.log(e);
					}
				}
			}
		}
		return false;
	}

}
