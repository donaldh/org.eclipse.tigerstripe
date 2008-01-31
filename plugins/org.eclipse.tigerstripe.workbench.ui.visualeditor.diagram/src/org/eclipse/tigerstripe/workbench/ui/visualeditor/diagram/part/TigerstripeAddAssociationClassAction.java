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

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.CompositeCommand;
import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.core.util.ViewType;
import org.eclipse.gmf.runtime.diagram.ui.actions.ActionIds;
import org.eclipse.gmf.runtime.diagram.ui.actions.AttachShapeAction;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsMessages;
import org.eclipse.gmf.runtime.diagram.ui.actions.internal.l10n.DiagramUIActionsPluginImages;
import org.eclipse.gmf.runtime.diagram.ui.commands.DeferredCreateConnectionViewCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ConnectionEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.INodeEditPart;
import org.eclipse.gmf.runtime.diagram.ui.internal.editparts.IEditableEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest.ViewAndElementDescriptor;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.emf.type.core.IHintedType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.dnd.ElementTypeMapper;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationClassEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AssociationEditPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Concrete implemention of AttachShapeAction which attaches a new
 * AssociationClass to the targeted Association.
 * 
 * @author tjmcs
 */
public class TigerstripeAddAssociationClassAction extends AttachShapeAction
		implements IObjectActionDelegate {

	/*
	 * local variables used in the command to update the AssociationClass'
	 * Association reference
	 */
	private AssociationClass associationClassRef;
	private Association associationRef;

	/**
	 * Constructor
	 * 
	 * @param page
	 *            the active workbenchPage.
	 */
	public TigerstripeAddAssociationClassAction(IWorkbenchPage page) {
		super(page);

	}

	public TigerstripeAddAssociationClassAction() {
		super(EclipsePlugin.getActivePage());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.action.IDisposableAction#init()
	 */
	@Override
	public void init() {
		super.init();
		setText(DiagramUIActionsMessages.AddNoteAction_ActionLabelText);
		setId(ActionIds.ACTION_ADD_NOTELINK);
		setToolTipText(DiagramUIActionsMessages.AddNoteAction_ActionToolTipText);
		setImageDescriptor(DiagramUIActionsPluginImages.DESC_NOTE_ATTACHMENT);
		setHoverImageDescriptor(getImageDescriptor());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.diagram.ui.actions.DiagramAction#calculateEnabled()
	 */
	@Override
	protected boolean calculateEnabled() {
		if (getSelectedObjects().isEmpty())
			return true;
		List parts = getSelectedObjects();
		for (int i = 0; i < parts.size(); i++) {
			Object o = parts.get(i);
			if (!(o instanceof INodeEditPart))
				return false;
			else {
				INodeEditPart nodeEditPart = (INodeEditPart) o;
				if (!(nodeEditPart.canAttachNote()))
					return false;

				// consider edit mode of selected edit part
				if (nodeEditPart instanceof IEditableEditPart
						&& !((IEditableEditPart) nodeEditPart)
								.isEditModeEnabled())
					return false;

				// consider edit mode of selected edit part's container
				EditPart parentPart = nodeEditPart.getParent();
				if (parentPart instanceof IEditableEditPart
						&& !((IEditableEditPart) parentPart)
								.isEditModeEnabled())
					return false;

			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gmf.runtime.common.ui.action.AbstractActionHandler#doRun(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	protected void doRun(IProgressMonitor progressMonitor) {
		List selectedEditParts = getSelectedObjects();
		IDiagramWorkbenchPart editor = getDiagramWorkbenchPart();

		// associationClass request
		CreateElementRequest crRequest = new CreateElementRequest(
				ElementTypeMapper.AssociationClass_3010);
		CreateElementRequestAdapter adapter = new CreateElementRequestAdapter(
				crRequest);
		ViewAndElementDescriptor viewAndElementDescriptor = new ViewAndElementDescriptor(
				adapter, Node.class,
				((IHintedType) ElementTypeMapper.AssociationClass_3010)
						.getSemanticHint(), getPreferencesHint());
		CreateViewAndElementRequest associationClassViewElemReq = new CreateViewAndElementRequest(
				viewAndElementDescriptor);

		associationClassViewElemReq.setLocation(getLocation(selectedEditParts));

		IGraphicalEditPart primaryPart = (IGraphicalEditPart) selectedEditParts
				.get(0);
		if (primaryPart instanceof ConnectionEditPart) {
			primaryPart = (IGraphicalEditPart) ((ConnectionEditPart) primaryPart)
					.getSource();
		}

		Command createAssociationClassCmd = getContainer(primaryPart)
				.getCommand(associationClassViewElemReq);

		// associationClass view adapter
		IAdaptable associationClassViewAdapter = (IAdaptable) ((List) associationClassViewElemReq
				.getNewObject()).get(0);

		// create the AssociationClass and Attachment commands
		CompositeCommand associationClassAttachmentCC = new CompositeCommand(
				getToolTipText());

		Iterator iter = selectedEditParts.iterator();
		AssociationEditPart associationEditPart = null;
		while (iter.hasNext()) {
			IGraphicalEditPart targetEditPart = (IGraphicalEditPart) iter
					.next();
			if (targetEditPart instanceof AssociationEditPart)
				associationEditPart = (AssociationEditPart) targetEditPart;
			associationClassAttachmentCC
					.compose(new DeferredCreateConnectionViewCommand(
							targetEditPart.getEditingDomain(),
							ViewType.NOTEATTACHMENT,
							associationClassViewAdapter, new EObjectAdapter(
									(EObject) targetEditPart.getModel()),
							editor.getDiagramGraphicalViewer(),
							getPreferencesHint()));
		}

		CompoundCommand cc = new CompoundCommand(getToolTipText());
		cc.add(createAssociationClassCmd);
		cc.add(new ICommandProxy(associationClassAttachmentCC));

		if (cc.canExecute()) {
			editor.getDiagramEditDomain().getDiagramCommandStack().execute(cc);
			IGraphicalEditPart editPart = editor.getDiagramEditPart();
			selectAddedObject(editor.getDiagramGraphicalViewer(),
					associationClassViewElemReq);
			List selectedObjects = super.getSelectedObjects();
			AssociationClassEditPart acEditPart = null;
			if (selectedObjects != null && selectedObjects.size() == 1) {
				Object selectedObject = selectedObjects.get(0);
				if (selectedObject instanceof AssociationClassEditPart)
					acEditPart = (AssociationClassEditPart) selectedObject;
			}
			associationClassRef = (AssociationClass) acEditPart
					.resolveSemanticElement();
			associationRef = (Association) associationEditPart
					.resolveSemanticElement();
			AbstractTransactionalCommand command = new AbstractTransactionalCommand(
					editPart.getEditingDomain(),
					"Change Association Reference", null) {
				@Override
				protected CommandResult doExecuteWithResult(
						IProgressMonitor monitor, IAdaptable info)
						throws ExecutionException {
					// associationClassRef.setAssociation(associationRef);
					// associationRef.setAssociationClassEnd(associationClassRef);
					return CommandResult.newOKCommandResult();
				}
			};
			try {
				OperationHistoryFactory.getOperationHistory().execute(command,
						new NullProgressMonitor(), null);
			} catch (ExecutionException e) {
				EclipsePlugin.log(e);
			}
		}
	}

	/*
	 * stuff below here is required for the IObjectActionDelegate interface
	 * implementation
	 */
	private AssociationEditPart mySelectedElement;
	private Shell myShell;

	/**
	 * 
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		myShell = targetPart.getSite().getShell();
		this.setWorkbenchPart(targetPart);
	}

	/**
	 * Creates a new AssociationClass object that is linked to the Association
	 * that is the "target" of this action
	 */
	public void run(IAction action) {

		IProgressMonitor progressMonitor = new NullProgressMonitor();
		this.doRun(progressMonitor);

	}

	/**
	 * 
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		mySelectedElement = null;
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			if (structuredSelection.size() == 1
					&& structuredSelection.getFirstElement() instanceof AssociationEditPart) {
				mySelectedElement = (AssociationEditPart) structuredSelection
						.getFirstElement();
			}
		}
		action.setEnabled(isEnabled());
	}

	/**
	 * 
	 */
	@Override
	public boolean isEnabled() {
		return super.isEnabled() && mySelectedElement != null;
	}

}
