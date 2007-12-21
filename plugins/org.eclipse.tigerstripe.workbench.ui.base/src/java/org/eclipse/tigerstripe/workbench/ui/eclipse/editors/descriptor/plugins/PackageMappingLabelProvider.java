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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.descriptor.plugins;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.core.plugin.PackageToSchemaMapper.PckXSDMapping;

public class PackageMappingLabelProvider implements ITableLabelProvider {

	public PackageMappingLabelProvider() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Image getColumnImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		PckXSDMapping mapping = (PckXSDMapping) element;

		switch (columnIndex) {
		case 0:
			return mapping.getPackage();
		case 1:
			return mapping.getXsdName();
		case 2:
			return mapping.getTargetNamespace();
		case 3:
			return mapping.getXsdLocation();
		case 4:
			return mapping.getUserPrefix();
		}
		return null;
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

}
