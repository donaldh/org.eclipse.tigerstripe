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
package org.eclipse.tigerstripe.workbench.ui.eclipse.dialogs;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.internal.api.contract.segment.IFacetReference;

public class FacetLabelProvider implements ILabelProvider, IColorProvider {

	public Image getImage(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getText(Object element) {
		if (element instanceof IFacetReference) {
			IFacetReference ref = (IFacetReference) element;
			String result = ref.getProjectRelativePath();
			if (ref.getContainingProject() != null) {
				result = result + " ("
						+ ref.getContainingProject().getProjectLabel() + ")";
			}
			return result;
		}

		return "unknown";
	}

	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	public Color getBackground(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	public Color getForeground(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

}
