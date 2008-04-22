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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.profile.artifacts;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.ArtifactSettingDetails;
import org.eclipse.tigerstripe.workbench.internal.core.util.Misc;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;

public class ArtifactDetailsLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	private Image getImage(boolean isSelected) {
		String key = isSelected ? Images.CHECKED_ICON
				: Images.UNCHECKED_ICON;
		return Images.get(key);
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return (columnIndex == 0) ? // COMPLETED_COLUMN?
		getImage(((ArtifactSettingDetails) element).isEnabled())
				: null;
	}

	public String getColumnText(Object element, int columnIndex) {
		String result = "";
		ArtifactSettingDetails details = (ArtifactSettingDetails) element;
		switch (columnIndex) {
		case 0: // CHECKED Column
			break;
		case 1:
			result = Misc.artifactTypeToLabel(details.getArtifactType());
			break;
		default:
			break;
		}
		return result;
	}

}
