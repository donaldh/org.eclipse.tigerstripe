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
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.NamedElementPropertiesHelper;
import org.eclipse.ui.IObjectActionDelegate;

public class AssociationShowAllAction extends BaseAssociationToggleAction
		implements IObjectActionDelegate {

	@Override
	public String getTargetPropertyKey() {
		return NamedElementPropertiesHelper.ASSOC_DETAILS;
	}

	@Override
	public String getTargetPropertyValue() {
		return NamedElementPropertiesHelper.ASSOC_SHOW_ALL;
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		super.selectionChanged(action, selection);

		Association[] assocs = getAssociations();

		if (assocs.length == 1) {
			Association assoc = assocs[0];
			if (assoc != null) {
				NamedElementPropertiesHelper helper = new NamedElementPropertiesHelper(
						assoc);
				if (helper.getProperty(getTargetPropertyKey()).equals(
						NamedElementPropertiesHelper.UNDEFINED)
						|| helper.getProperty(getTargetPropertyKey()).equals(
								getTargetPropertyValue())) {
					action.setChecked(true);
				} else
					action.setChecked(false);
			}
		} else {
			boolean allChecked = true;
			for (Association assoc : assocs) {
				if (assoc != null) {
					NamedElementPropertiesHelper helper = new NamedElementPropertiesHelper(
							assoc);
					if (!helper.getProperty(getTargetPropertyKey()).equals(
							NamedElementPropertiesHelper.UNDEFINED)
							&& !helper.getProperty(getTargetPropertyKey())
									.equals(getTargetPropertyValue())) {
						allChecked = false;
					}
				}
			}
			action.setChecked(allChecked);
		}
	}

}
