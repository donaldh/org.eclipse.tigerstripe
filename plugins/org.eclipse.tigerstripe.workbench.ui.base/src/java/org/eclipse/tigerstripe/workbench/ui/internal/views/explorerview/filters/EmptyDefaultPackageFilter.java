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

import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * Filters out the default package if it is empty.
 * 
 * @author Eric Dillon
 * @since 1.0
 */
public class EmptyDefaultPackageFilter extends ViewerFilter {

	public EmptyDefaultPackageFilter() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof IPackageFragment) {
			IPackageFragment pack = (IPackageFragment) element;
			try {
				if ("".equals(pack.getElementName()) && !pack.hasChildren())
					return false;
			} catch (JavaModelException e) {
				return true;
			}
		}
		return true;
	}

}
