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
package org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.filters;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * Filters out all the .* files from a view
 * 
 * @author Eric Dillon
 * @since 0.3.6
 */
public class DottedFilesFilter extends ViewerFilter {

	public DottedFilesFilter() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof IFile) {
			IFile file = (IFile) element;
			String name = file.getName();

			return !name.startsWith(".");
		} else if (element instanceof IFolder) {
			IFolder folder = (IFolder) element;
			String name = folder.getName();
			return !name.startsWith(".");
		} else
			return true;
	}

}
