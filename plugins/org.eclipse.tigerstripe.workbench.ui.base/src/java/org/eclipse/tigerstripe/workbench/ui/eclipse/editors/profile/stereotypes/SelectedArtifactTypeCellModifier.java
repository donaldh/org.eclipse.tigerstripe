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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.profile.stereotypes;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.workbench.internal.core.profile.stereotype.StereotypeScopeDetails;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.profile.stereotypes.StereotypeDetailsPage.SelectedArtifactType;

public class SelectedArtifactTypeCellModifier implements ICellModifier {

	private StereotypeDetailsPage section;

	SelectedArtifactTypeCellModifier(StereotypeDetailsPage section) {
		this.section = section;
	}

	public boolean canModify(Object element, String property) {
		return true;
	}

	public Object getValue(Object element, String property) {
		// Find the index of the column
		int columnIndex = section.getColumnNames().indexOf(property);

		Object result = null;
		SelectedArtifactType details = (SelectedArtifactType) element;

		switch (columnIndex) {
		case 0: // COMPLETED_COLUMN
			result = new Boolean(details.isSelected);
			break;
		case 1: // DESCRIPTION_COLUMN
			result = details.artifactTypeName;
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
		SelectedArtifactType task = (SelectedArtifactType) item.getData();

		switch (columnIndex) {
		case 0: // CHECKED COlumn
			task.isSelected = (((Boolean) value).booleanValue());

			StereotypeScopeDetails details = (StereotypeScopeDetails) section
					.getIStereotype().getStereotypeScopeDetails();

			if (task.isSelected) {
				details.addArtifactLevelType(task.artifactTypeName);
			} else {
				details.removeArtifactLevelType(task.artifactTypeName);
			}
			section.getMaster().markPageModified();
			break;
		default:
		}
		section.refresh();
	}

}
