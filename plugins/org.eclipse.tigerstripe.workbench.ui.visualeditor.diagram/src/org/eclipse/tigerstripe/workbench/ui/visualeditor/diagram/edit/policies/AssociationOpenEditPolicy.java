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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.policies;

import java.util.Map;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.OpenEditPolicy;
import org.eclipse.gmf.runtime.notation.impl.EdgeImpl;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.dialogs.AssociationPropertiesEditDialog;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.commands.AssociationUpdateCommand;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MapEditPart;

public class AssociationOpenEditPolicy extends OpenEditPolicy {

	@Override
	protected Command getOpenCommand(Request req) {
		EditPart editPart = getHost();
		// host of this action could be the AssociationEditPart or it could be
		// one of the children for the AssociationEditPart; make sure we
		// find the AssociationEditPart reference here...
		EditPart associationEditPart = editPart;
		while (!(associationEditPart instanceof AssociationEditPart)
				&& !(associationEditPart instanceof AssociationClassEditPart)
				&& !(associationEditPart instanceof MapEditPart)) {
			associationEditPart = associationEditPart.getParent();
		}
		// when get to here, should have a reference to the AssociationEditPart,
		// if not
		// return immediately (shouldn't happen, but you never know)
		if (associationEditPart instanceof MapEditPart)
			return null;
		MapEditPart mapEditPart = null;
		if (associationEditPart instanceof AssociationEditPart)
			mapEditPart = (MapEditPart) ((AssociationEditPart) associationEditPart)
					.getSource().getParent();
		else
			mapEditPart = (MapEditPart) ((AssociationClassEditPart) associationEditPart)
					.getSource().getParent();
		EdgeImpl edgeImpl = (EdgeImpl) associationEditPart.getModel();
		final Association association = (Association) edgeImpl.getElement();
		Shell shell = EclipsePlugin.getActiveWorkbenchShell();
		AssociationPropertiesEditDialog aed = null;
		try {
			aed = new AssociationPropertiesEditDialog(shell, mapEditPart,
					association);
		} catch (RuntimeException e) {
			return null;
		}
		int retVal = aed.open();
		if (retVal != IDialogConstants.OK_ID)
			return null;
		Map<String, Object> changedValuesMap = aed.getChangedValuesMap();
		TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
				.getEditingDomain();
		AssociationUpdateCommand cmd = new AssociationUpdateCommand(
				editingDomain, mapEditPart, association, changedValuesMap);
		return new ICommandProxy(cmd);
	}

}
