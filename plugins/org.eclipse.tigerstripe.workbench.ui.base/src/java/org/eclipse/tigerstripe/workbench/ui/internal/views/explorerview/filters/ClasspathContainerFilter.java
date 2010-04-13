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

import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.internal.ui.packageview.ClassPathContainer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.tigerstripe.workbench.internal.core.classpath.IReferencesConstants;

/**
 * Filters out all the .* files from a view
 * 
 * @author Eric Dillon
 * @since 0.3.6
 */
public class ClasspathContainerFilter extends ViewerFilter {

	public ClasspathContainerFilter() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof ClassPathContainer) {
			// Filter all classpath containers except model references
			ClassPathContainer container = (ClassPathContainer) element;
			IClasspathEntry entry = container.getClasspathEntry();
			if (entry != null
					&& IReferencesConstants.REFERENCES_CONTAINER_PATH
							.equals(entry.getPath())) {
				return container.getChildren().length > 0;
			}
			return false;
		}
		return true;
	}

}
