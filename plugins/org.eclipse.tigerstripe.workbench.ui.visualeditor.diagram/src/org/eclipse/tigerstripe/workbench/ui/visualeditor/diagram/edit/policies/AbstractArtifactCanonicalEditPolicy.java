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

import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.gmf.runtime.diagram.ui.editparts.ITextAwareEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MapEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.NamePackageInterface;

public abstract class AbstractArtifactCanonicalEditPolicy extends
		CanonicalEditPolicy {

	@Override
	protected void handleNotificationEvent(Notification arg0) {
		// all we need is to make sure the EditMapPart is refreshed.
		((MapEditPart) getHost().getParent()).refreshCanonicalPolicy();
		if (arg0.getFeature() instanceof EAttribute) {
			EAttribute attr = (EAttribute) arg0.getFeature();
			if ("isAbstract".equals(attr.getName())) {
				List children = getHost().getChildren();
				for (Object child : children) {
					if (child instanceof ITextAwareEditPart
							&& child instanceof NamePackageInterface) {
						ITextAwareEditPart part = (ITextAwareEditPart) child;
						part.refresh();
					}
				}
			}
		}

		super.handleNotificationEvent(arg0);
	}

	// @Override
	// protected void handleNotificationEvent(Notification arg0) {
	// if (arg0.getEventType() == Notification.SET
	// && arg0.getFeature() == VisualeditorPackage.eINSTANCE
	// .getAbstractArtifact_Extends()) {
	// getHost().getParent().refresh();
	// // handleExtendsSET(arg0);
	// } else if (arg0.getEventType() == Notification.ADD
	// && arg0.getFeature() == VisualeditorPackage.eINSTANCE
	// .getAbstractArtifact_References()) {
	// getHost().getParent().refresh();
	// // handleReferencesADD(arg0);
	// }
	// super.handleNotificationEvent(arg0);
	// }
	//
	// protected void handleReferencesADD(Notification arg0) {
	// Object oldValue = arg0.getOldValue();
	// Reference newValue = (Reference) arg0.getNewValue();
	//
	// final IGraphicalEditPart host = (IGraphicalEditPart) getHost();
	// final EditPart newTargetPart = host.findEditPart(host.getParent(),
	// newValue.getZEnd());
	// if (newTargetPart != null) {
	// // we need to create it
	// ConnectionViewDescriptor descriptor = new ConnectionViewDescriptor(
	// new EObjectAdapter(newValue),
	// ((IHintedType) TigerstripeElementTypes.Reference_3010)
	// .getSemanticHint(),
	// ((IGraphicalEditPart) getHost())
	// .getDiagramPreferencesHint());
	// CreateConnectionViewRequest createRequest = new
	// CreateConnectionViewRequest(
	// descriptor);
	// createRequest.getExtendedData().put("meuh", Boolean.TRUE);
	// createRequest.setSourceEditPart(getHost());
	// createRequest.setTargetEditPart(newTargetPart);
	// createRequest.setType(RequestConstants.REQ_CONNECTION_START);
	// getHost().getCommand(createRequest);
	// createRequest.setType(RequestConstants.REQ_CONNECTION_END);
	// Command cmd = newTargetPart.getCommand(createRequest);
	// ((IGraphicalEditPart) getHost()).getDiagramEditDomain()
	// .getDiagramCommandStack().execute(cmd);
	// }
	// }
	//
	// protected void handleExtendsSET(Notification arg0) {
	// Object oldValue = arg0.getOldValue();
	// Object newValue = arg0.getNewValue();
	//
	// final IGraphicalEditPart host = (IGraphicalEditPart) getHost();
	//
	// AbstractArtifactExtendsEditPart extendsPart = null;
	// List list = host.getSourceConnections();
	// for (Object obj : list) {
	// if (obj instanceof AbstractArtifactExtendsEditPart) {
	// extendsPart = (AbstractArtifactExtendsEditPart) obj;
	// }
	// }
	//
	// final AbstractArtifactExtendsEditPart fExtendsPart =
	// (AbstractArtifactExtendsEditPart) extendsPart;
	// if (fExtendsPart != null) {
	// final EditPart newTargetPart = host.findEditPart(host.getParent(),
	// (EObject) newValue);
	// if (newTargetPart != null) {
	// final Edge e = (Edge) fExtendsPart.getModel();
	// AbstractTransactionalCommand command = new AbstractTransactionalCommand(
	// host.getEditingDomain(), "Update Extends", null) {
	// protected CommandResult doExecuteWithResult(
	// IProgressMonitor monitor, IAdaptable info)
	// throws ExecutionException {
	// e.setTarget((Node) newTargetPart.getModel());
	// fExtendsPart.refresh();
	// return CommandResult.newOKCommandResult();
	// }
	// };
	// try {
	// OperationHistoryFactory.getOperationHistory().execute(
	// command, new NullProgressMonitor(), null);
	// } catch (ExecutionException ee) {
	// EclipsePlugin.log(ee);
	// }
	// } else {
	// Command cmd = getDeleteViewCommand(fExtendsPart
	// .getPrimaryView());
	// ((IGraphicalEditPart) getHost()).getDiagramEditDomain()
	// .getDiagramCommandStack().execute(cmd);
	// }
	// } else {
	// // we need to create it
	// CreateElementRequest req = new CreateElementRequest(
	// TigerstripeElementTypes.AbstractArtifactExtends_3007);
	// CreateElementRequestAdapter adapter = new CreateElementRequestAdapter(
	// req);
	// ConnectionViewDescriptor descriptor = new ConnectionViewDescriptor(
	// adapter,
	// ((IHintedType) TigerstripeElementTypes.AbstractArtifactExtends_3007)
	// .getSemanticHint(),
	// ((IGraphicalEditPart) getHost())
	// .getDiagramPreferencesHint());
	// CreateConnectionViewRequest createRequest = new
	// CreateConnectionViewRequest(
	// descriptor);
	// createRequest.getExtendedData().put("meuh", Boolean.TRUE);
	// final EditPart newTargetPart = host.findEditPart(host.getParent(),
	// (EObject) newValue);
	// createRequest.setSourceEditPart(getHost());
	// createRequest.setTargetEditPart(newTargetPart);
	// createRequest.setType(RequestConstants.REQ_CONNECTION_START);
	// getHost().getCommand(createRequest);
	// createRequest.setType(RequestConstants.REQ_CONNECTION_END);
	// Command cmd = newTargetPart.getCommand(createRequest);
	// ((IGraphicalEditPart) getHost()).getDiagramEditDomain()
	// .getDiagramCommandStack().execute(cmd);
	// }
	// }
}
