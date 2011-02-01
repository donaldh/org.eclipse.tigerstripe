/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionNodeEditPart;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.DiagramProperty;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstancediagramFactory;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.PropertyAwarePart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part.BaseDiagramPartAction;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.util.NamedElementPropertiesHelper;

public abstract class BaseAssociationToggleAction extends BaseDiagramPartAction {

	public BaseAssociationToggleAction() {
	}

	protected AssociationInstance[] getAssociations() {
		List<AssociationInstance> result = new ArrayList<AssociationInstance>();
		EObject obj = getCorrespondingEObject();
		if (obj instanceof AssociationInstance) {
			result.add((AssociationInstance) obj);
		}
		return result.toArray(new AssociationInstance[result.size()]);
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		super.selectionChanged(action, selection);

		AssociationInstance[] assocs = getAssociations();
		// All need to be associations
		if (assocs != null && assocs.length != 0) {
			boolean enabled = true;
			for (AssociationInstance assoc : assocs) {
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
		TransactionalEditingDomain editDomain = ((ConnectionNodeEditPart) mySelectedElement)
				.getEditingDomain();
		final Command cmd = new AbstractCommand() {
			@Override
			public boolean canExecute() {
				return true;
			}

			public void execute() {
				AssociationInstance[] assocs = getAssociations();

				// Set the value on the assoc
				for (AssociationInstance assoc : assocs) {
					NamedElementPropertiesHelper helper = new NamedElementPropertiesHelper(
							assoc);

					DiagramProperty prop = helper.setProperty(
							getTargetPropertyKey(), getTargetPropertyValue());
				}

				// refresh the diagram
				DiagramProperty newProp = InstancediagramFactory.eINSTANCE
						.createDiagramProperty();
				newProp.setName(getTargetPropertyKey());
				newProp.setValue(getTargetPropertyValue());
				if (mySelectedElement instanceof PropertyAwarePart) {
					PropertyAwarePart pApart = (PropertyAwarePart) mySelectedElement;
					pApart.propertyChanged(newProp);
				}
			}

			public void redo() {
			}

			@Override
			public boolean canUndo() {
				return false;
			}
		};

		editDomain.getCommandStack().execute(cmd);
		editDomain.getCommandStack().flush();
	}

	public abstract String getTargetPropertyKey();

	public abstract String getTargetPropertyValue();
}
