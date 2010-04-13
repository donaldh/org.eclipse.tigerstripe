/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.filters;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.tigerstripe.workbench.internal.core.module.InstalledModuleManager;

/**
 * The TSLibraryFilter is a viewer filter used to filter Java libraries except
 * installed modules
 */
public class TSLibraryFilter extends ViewerFilter {

	/*
	 * (non-Javadoc) Method declared on ViewerFilter.
	 */
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof IPackageFragmentRoot) {
			IPackageFragmentRoot root = (IPackageFragmentRoot) element;
			if (root.isArchive()) {
				// don't filter out JARs contained in the project itself
				IResource resource = root.getResource();
				if (resource != null) {
					IProject jarProject = resource.getProject();
					IProject container = root.getJavaProject().getProject();
					return container.equals(jarProject);
				} else {
					return InstalledModuleManager.getInstance().getModule(
							root.getPath()) != null;
				}
			}
		}
		return true;
	}
}
