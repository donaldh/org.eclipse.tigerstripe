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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.DiagramProperty;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.PropertyAwarePart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.BaseDiagramPartAction;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.NamedElementPropertiesHelper;

public abstract class BaseAssociationToggleAction extends BaseDiagramPartAction {

	public BaseAssociationToggleAction() {
		// TODO Auto-generated constructor stub
	}

	protected Association[] getAssociations() {
		List<Association> result = new ArrayList<Association>();
		EObject[] objs = getCorrespondingEObjects();
		for (EObject obj : objs) {
			if (obj instanceof Association) {
				result.add((Association) obj);
			}
		}
		return result.toArray(new Association[result.size()]);
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		super.selectionChanged(action, selection);

		Association[] assocs = getAssociations();
		// All need to be associations
		if (assocs != null && assocs.length != 0
				&& assocs.length == mySelectedElements.length) {
			boolean enabled = true;
			for (Association assoc : assocs) {
				NamedElementPropertiesHelper helper = new NamedElementPropertiesHelper(
						assoc);
				if (!helper.getProperty(getTargetPropertyKey()).equals(
						getTargetPropertyValue())) {
					enabled = false;
					break;
				}
			}
			action.setChecked(enabled);
		}
	}

	public void run(IAction action) {

		TransactionalEditingDomain editDomain = ((ConnectionNodeEditPart) mySelectedElements[0])
				.getEditingDomain();
		final Command cmd = new AbstractCommand() {
			@Override
			public boolean canExecute() {
				return true;
			}

			public void execute() {
				Association[] assocs = getAssociations();

				// Set the value on the assoc
				for (Association assoc : assocs) {
					NamedElementPropertiesHelper helper = new NamedElementPropertiesHelper(
							assoc);

					DiagramProperty prop = helper.setProperty(
							getTargetPropertyKey(), getTargetPropertyValue());
				}

				// refresh the diagram
				DiagramProperty newProp = VisualeditorFactory.eINSTANCE
						.createDiagramProperty();
				newProp.setName(getTargetPropertyKey());
				newProp.setValue(getTargetPropertyValue());
				for (EditPart part : mySelectedElements) {
					if (part instanceof PropertyAwarePart) {
						PropertyAwarePart pApart = (PropertyAwarePart) part;
						pApart.propertyChanged(newProp);
					}
				}
			}

			public void redo() {

			}

			@Override
			public boolean canUndo() {
				return false;
			}
		};

		try {
			BaseETAdapter.setIgnoreNotify(true);
			editDomain.getCommandStack().execute(cmd);
			editDomain.getCommandStack().flush();
		} finally {
			BaseETAdapter.setIgnoreNotify(false);
		}

	}

	public abstract String getTargetPropertyKey();

	public abstract String getTargetPropertyValue();
}
