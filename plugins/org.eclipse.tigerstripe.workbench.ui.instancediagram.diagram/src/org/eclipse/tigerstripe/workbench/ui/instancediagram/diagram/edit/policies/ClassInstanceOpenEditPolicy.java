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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.policies;

import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.OpenEditPolicy;
import org.eclipse.gmf.runtime.notation.impl.NodeImpl;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.dialogs.ClassInstanceEditDialog;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.commands.ClassInstanceUpdateCommand;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.ClassInstanceEditPart;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.parts.InstanceMapEditPart;

public class ClassInstanceOpenEditPolicy extends OpenEditPolicy {

	@Override
	protected Command getOpenCommand(Request req) {
		EditPart editPart = getHost();
		// host of this action could be the ClassInstanceEditPart or it could be
		// one of the compartments for the ClassInstanceEditPart; make sure we
		// find the ClassInstancEditPart reference here...
		EditPart classInstanceEditPart = editPart;
		while (!(classInstanceEditPart instanceof ClassInstanceEditPart)
				&& !(classInstanceEditPart instanceof InstanceMapEditPart)) {
			classInstanceEditPart = classInstanceEditPart.getParent();
		}
		// when get to here, should have a reference to the
		// ClassInstanceEditPart, if not
		// return immediately (shouldn't happen, but you never know
		if (classInstanceEditPart instanceof InstanceMapEditPart)
			return null;
		InstanceMapEditPart mapEditPart = (InstanceMapEditPart) classInstanceEditPart
				.getParent();
		NodeImpl nodeImpl = (NodeImpl) classInstanceEditPart.getModel();
		final ClassInstance classInstance = (ClassInstance) nodeImpl
				.getElement();
		Shell shell = EclipsePlugin.getActiveWorkbenchShell();
		ClassInstanceEditDialog ied = null;
		try {
			ied = new ClassInstanceEditDialog(shell, classInstance, mapEditPart);
		} catch (RuntimeException e) {
			return null;
		}
		int retVal = ied.open();
		if (retVal != IDialogConstants.OK_ID)
			return null;

		HashMap<String, List<Object>> newVariableMap = ied.getSelection();
		String newName = ied.getInstanceName();
		TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
				.getEditingDomain();
		// final InstanceMap instanceMap =
		// (InstanceMap)((View)mapEditPart.getModel()).getElement();
		ClassInstanceUpdateCommand cmd = new ClassInstanceUpdateCommand(
				editingDomain, mapEditPart, classInstance, newName,
				newVariableMap, ied.getNewReferenceInstances());
		return new ICommandProxy(cmd);
	}

}
