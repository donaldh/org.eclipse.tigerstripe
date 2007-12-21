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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.profile.artifacts;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.core.profile.properties.ArtifactSettingDetails;

public class ArtifactDetailsCellModifier implements ICellModifier {

	private CoreArtifactsSection section;

	public ArtifactDetailsCellModifier(CoreArtifactsSection section) {
		this.section = section;
	}

	public boolean canModify(Object element, String property) {
		return true;
	}

	public Object getValue(Object element, String property) {
		// Find the index of the column
		int columnIndex = section.getColumnNames().indexOf(property);

		Object result = null;
		ArtifactSettingDetails details = (ArtifactSettingDetails) element;

		switch (columnIndex) {
		case 0: // COMPLETED_COLUMN
			result = new Boolean(details.isEnabled());
			break;
		case 1: // DESCRIPTION_COLUMN
			result = details.getArtifactType();
			break;
		default:
			result = "";
		}
		return result;
	}

	public void modify(Object element, String property, Object value) {
		// Find the index of the column
		int columnIndex = section.getColumnNames().indexOf(property);

		TableItem item = (TableItem) element;
		ArtifactSettingDetails task = (ArtifactSettingDetails) item.getData();

		switch (columnIndex) {
		case 0: // CHECKED COlumn
			task.setEnabled(((Boolean) value).booleanValue());
			section.markPageModified();
			break;
		default:
		}
		section.refresh();
	}

}
