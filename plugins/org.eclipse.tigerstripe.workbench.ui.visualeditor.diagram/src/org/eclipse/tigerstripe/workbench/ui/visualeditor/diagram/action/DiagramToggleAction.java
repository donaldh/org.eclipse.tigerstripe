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

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MapEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part.BaseDiagramPartAction;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.DiagramPropertiesHelper;
import org.eclipse.ui.IObjectActionDelegate;

public abstract class DiagramToggleAction extends BaseDiagramPartAction
		implements IObjectActionDelegate {

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		mySelectedElements = new EditPart[0];
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.size() == 1) {
				if (structuredSelection.getFirstElement() instanceof MapEditPart) {
					mySelectedElements = new EditPart[] { (EditPart) structuredSelection
							.getFirstElement() };
				}
			}
		}

		if (mySelectedElements.length != 0
				&& mySelectedElements[0] instanceof MapEditPart) {
			action.setEnabled(true);

			boolean checked = Boolean.parseBoolean(getHelper()
					.getPropertyValue(getTargetProperty()));
			action.setChecked(checked);
		} else {
			action.setEnabled(false);
		}
	}

	private DiagramPropertiesHelper getHelper() {
		MapEditPart mapEditPart = (MapEditPart) mySelectedElements[0];
		Map map = (Map) ((Diagram) mapEditPart.getModel()).getElement();
		DiagramPropertiesHelper helper = new DiagramPropertiesHelper(map);

		return helper;
	}

	public void run(IAction action) {

		TransactionalEditingDomain editDomain = ((MapEditPart) mySelectedElements[0])
				.getEditingDomain();
		final boolean isChecked = action.isChecked();
		Command cmd = new AbstractCommand() {
			@Override
			public boolean canExecute() {
				return true;
			}

			public void execute() {
				getHelper().setProperty(getTargetProperty(),
						String.valueOf(isChecked));
				MapEditPart mapEditPart = (MapEditPart) mySelectedElements[0];
				mapEditPart.diagramPropertyChanged(getHelper()
						.getDiagramProperty(getTargetProperty()));
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

	protected abstract String getTargetProperty();
}
