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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.part;

import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.actions.AbstractDeleteFromAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.l10n.DiagramUIMessages;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AbstractArtifactExtendsEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AbstractArtifactImplementsEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassClassEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DatatypeArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.DependencyEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.EnumerationEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ExceptionArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.ManagedEntityArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NamedQueryArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NotificationArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.SessionFacadeArtifactEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.UpdateProcedureArtifactEditPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

public class TSRemoveFromDiagramAction extends AbstractDeleteFromAction
		implements IObjectActionDelegate {

	/**
	 * Returns true if the given part can be delete from the diagram without
	 * causing inconsistencies with the model
	 * 
	 * @param part
	 * @return
	 */
	public static boolean canBeDeletedFromDiagram(Object part) {
		return (part instanceof ManagedEntityArtifactEditPart
				|| part instanceof DatatypeArtifactEditPart
				|| part instanceof EnumerationEditPart
				|| part instanceof ExceptionArtifactEditPart
				|| part instanceof SessionFacadeArtifactEditPart
				|| part instanceof NamedQueryArtifactEditPart
				|| part instanceof NotificationArtifactEditPart
				|| part instanceof UpdateProcedureArtifactEditPart
				|| part instanceof AssociationEditPart
				|| part instanceof AssociationClassEditPart
				|| part instanceof AssociationClassClassEditPart
				|| part instanceof DependencyEditPart
				|| part instanceof AbstractArtifactExtendsEditPart || part instanceof AbstractArtifactImplementsEditPart);
	}

	public TSRemoveFromDiagramAction() {
		super(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().getActivePart());
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	public void selectionChanged(IAction action, ISelection selection) {
		refresh();
		action.setEnabled(isEnabled());
	}

	public void run(IAction action) {
		if (isEnabled())
			execute(getCommand(), new NullProgressMonitor());
	}

	/*
	 * NOTE: Grabbed the following code from the DeleteFromModelAction class in
	 * the GMF v2.0 distribution (so that we could extend the
	 * AbstractDeleteFromAction class instead, which doesn't move to a different
	 * package between GMF v1.0.x and v2.0)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#calculateEnabled
	 * ()
	 */
	@Override
	protected boolean calculateEnabled() {
		@SuppressWarnings("unchecked")
		List operationSet = getOperationSet();
		if (operationSet.isEmpty())
			return false;
		Request request = getTargetRequest();
		@SuppressWarnings("unchecked")
		Iterator editParts = operationSet.iterator();
		while (editParts.hasNext()) {
			EditPart editPart = (EditPart) editParts.next();

			if (!canBeDeletedFromDiagram(editPart))
				return false;

			if (editPart instanceof AbstractArtifactExtendsEditPart
					|| editPart instanceof AbstractArtifactImplementsEditPart) {
				return true;
			} else if (editPart instanceof IGraphicalEditPart) {
				IGraphicalEditPart gEditPart = (IGraphicalEditPart) editPart;
				View view = (View) gEditPart.getModel();
				// Disallow diagram deletion from model only if it is the top
				// most diagram
				EObject container = view.eContainer();
				EObject element = ViewUtil.resolveSemanticElement(view);
				if ((element == null)
						|| (element.eIsProxy())
						|| (element instanceof Diagram)
						|| (view instanceof Diagram && (container == null || !(container instanceof View))))
					return false;
			} else {
				Command curCommand = editPart.getCommand(request);
				if (curCommand == null || (curCommand.canExecute() == false))
					return false;
			}
		}
		return true;
	}

	/**
	 * Initializes this action's text and images.
	 */
	@Override
	public void init() {
		super.init();
		setId(ActionIds.ACTION_DELETE_FROM_MODEL);
		setText(DiagramUIMessages.DiagramEditor_Delete_from_Model);
		setToolTipText(DiagramUIMessages.DiagramEditor_Delete_from_ModelToolTip);
		ISharedImages workbenchImages = PlatformUI.getWorkbench()
				.getSharedImages();
		setHoverImageDescriptor(workbenchImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		setImageDescriptor(workbenchImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		setDisabledImageDescriptor(workbenchImages
				.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#getCommand(org
	 * .eclipse.gef.Request)
	 */
	@Override
	protected Command getCommand(Request request) {
		CompositeTransactionalCommand command = new CompositeTransactionalCommand(
				getEditingDomain(), getCommandLabel());

		@SuppressWarnings("unchecked")
		List<EditPart> selectedParts = getSelectedObjects();
		for (EditPart selectedPart : selectedParts) {
			Command curCommand = selectedPart.getCommand(request);
			if (curCommand != null) {
				command.compose(new CommandProxy(curCommand));
			}
		}

		if ((command.isEmpty()))
			return UnexecutableCommand.INSTANCE;
		return new ICommandProxy(command);
	}

	@Override
	protected String getCommandLabel() {
		return DiagramUIMessages.DiagramEditor_Delete_from_Model;
	};

}
