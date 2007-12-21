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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.action;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.NamedElementPropertiesHelper;
import org.eclipse.ui.IObjectActionDelegate;

public class AssociationShowZEndNameAction extends BaseAssociationToggleAction
		implements IObjectActionDelegate {

	@Override
	public String getTargetPropertyKey() {
		return NamedElementPropertiesHelper.ASSOC_DETAILS;
	}

	@Override
	public String getTargetPropertyValue() {
		return NamedElementPropertiesHelper.ASSOC_SHOW_NAMEZEND;
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		super.selectionChanged(action, selection);

		Association[] assocs = getAssociations();

		if (assocs.length == 1) {
			if (assocs[0] instanceof AssociationClass) {
				action.setText("\"" + assocs[0].getZEndName() + "\" only.");
			} else if (assocs[0] instanceof Association) {
				action.setText("Assoc. Name && \"" + assocs[0].getZEndName()
						+ "\"");
			}
			NamedElementPropertiesHelper helper = new NamedElementPropertiesHelper(
					assocs[0]);
			action.setChecked(helper.getProperty(getTargetPropertyKey())
					.equals(getTargetPropertyValue()));
		} else {
			boolean allChecked = true;
			for (Association assoc : assocs) {
				NamedElementPropertiesHelper helper = new NamedElementPropertiesHelper(
						assoc);
				allChecked = allChecked
						& helper.getProperty(getTargetPropertyKey()).equals(
								getTargetPropertyValue());
				if (assoc instanceof AssociationClass) {
				}
			}
			action.setChecked(allChecked);
			action.setText("Assoc. Name && 'zEnd'");
		}
	}
}
