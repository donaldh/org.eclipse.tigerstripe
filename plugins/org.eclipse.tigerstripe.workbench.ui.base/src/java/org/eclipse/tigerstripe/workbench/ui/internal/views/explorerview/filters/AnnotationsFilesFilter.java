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
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.filters;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.workbench.internal.api.ITigerstripeConstants;

/**
 * Filters out all the .* files from a view
 * 
 * @author Eric Dillon
 * @since 0.3.6
 */
public class AnnotationsFilesFilter extends ViewerFilter {

	private boolean hide = true;

	public AnnotationsFilesFilter() {
		super();
	}

	public void setHide(boolean hide) {
		this.hide = hide;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof IFile) {
			IFile file = (IFile) element;
			if (AnnotationPlugin.getManager().isAnnotationFile(file)) {
				return !hide;
			}
			return true;
		} else if (element instanceof IFolder) {
			IFolder folder = (IFolder) element;
			String name = folder.getName();
			if (ITigerstripeConstants.ANNOTATION_DIR.equals(name)) {
				return !hide;
			}
			return true;
		} else
			return true;
	}
}
