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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.dnd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.diagram.core.edithelpers.CreateElementRequestAdapter;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.DiagramDragDropEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.requests.ArrangeRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewAndElementRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RefreshConnectionsRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewAndElementRequest.ConnectionViewAndElementDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest.ViewAndElementDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.IHintedType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.model.IRelationship;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.commands.PostCreationModelUpdateCommand;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.MapHelper;

public class ClassDiagramDragDropEditPolicy extends DiagramDragDropEditPolicy {

	public final static String DRAGGED_ARTIFACT = "draggedArtifact";

	@Override
	protected Command createViewsAndArrangeCommand(
			DropObjectsRequest dropRequest, List viewDescriptors) {

		List allObjects = new ArrayList(); // to contain all final objects

		// We need to create 1 composite command that includes all the incoming
		// requests. There might be multiple viewDescriptors if multiple objects
		// are
		// being dropped at once onto the diagram
		CompoundCommand compoundCommand = new CompoundCommand("DragNDrop");

		for (ViewDescriptor descriptor : (List<ViewDescriptor>) viewDescriptors) {
			if (descriptor instanceof ViewAndElementDescriptor) {
				ViewAndElementDescriptor desc = (ViewAndElementDescriptor) descriptor;
				CreateViewAndElementRequest createViewRequest = new CreateViewAndElementRequest(
						desc);

				allObjects.addAll((List) createViewRequest.getNewObject());

				if (dropRequest.getLocation() != null)
					createViewRequest.setLocation(dropRequest.getLocation());

				Command createCommand = getHost().getCommand(createViewRequest);
				if (createCommand != null) {
					compoundCommand.add(createCommand);
				}
				// } else if (descriptor instanceof ConnectionViewDescriptor) {
				//
				// ConnectionViewDescriptor desc = (ConnectionViewDescriptor)
				// descriptor;
				// CreateConnectionViewRequest createRequest = new
				// CreateConnectionViewRequest(
				// desc);
				//
				// CreateElementRequest req = (CreateElementRequest) desc
				// .getElementAdapter().getAdapter(
				// CreateElementRequest.class);
				// IAbstractArtifact artifact = (IAbstractArtifact) req
				// .getParameter("IAbstractArtifact");
				// String fqn = artifact.getFullyQualifiedName();
				// MapHelper helper = new MapHelper((Map) ((Diagram) getHost()
				// .getModel()).getElement());
				// EObject targetObject = helper.findAbstractArtifactFor(fqn);
				//				
				// // final EditPart newTargetPart = ((Diagram)
				// getHost()).findEditPart(getHost()
				// // .getParent(), (EObject) targetObject);
				// // createRequest.setSourceEditPart(getHost());
				// // createRequest.setTargetEditPart(newTargetPart);
				// //
				// createRequest.setType(RequestConstants.REQ_CONNECTION_START);
				// // getHost().getCommand(createRequest);
				// //
				// createRequest.setType(RequestConstants.REQ_CONNECTION_END);
				// // Command cmd = newTargetPart.getCommand(createRequest);
				//
				//				
			} else if (descriptor instanceof ConnectionViewAndElementDescriptor) {
				// do we need to check that both ends are available?
				ConnectionViewAndElementDescriptor desc = (ConnectionViewAndElementDescriptor) descriptor;
				CreateConnectionViewAndElementRequest createViewRequest = new CreateConnectionViewAndElementRequest(
						desc);
				IRelationship iArtifact = (IRelationship) ((CreateElementRequest) desc
						.getCreateElementRequestAdapter().getAdapter(
								CreateElementRequest.class))
						.getParameter("IAbstractArtifact");

				String aEndType = iArtifact.getRelationshipAEnd().getType()
						.getFullyQualifiedName();
				String zEndType = iArtifact.getRelationshipZEnd().getType()
						.getFullyQualifiedName();

				MapHelper helper = new MapHelper((Map) ((Diagram) getHost()
						.getModel()).getElement());
				EObject aEnd = helper.findAbstractArtifactFor(aEndType);
				EObject zEnd = helper.findAbstractArtifactFor(zEndType);

				if (aEnd != null && zEnd != null) {

					final EditPart targetPart = ((IGraphicalEditPart) getHost())
							.findEditPart(getHost(), zEnd);
					final EditPart srcPart = ((IGraphicalEditPart) getHost())
							.findEditPart(getHost(), aEnd);

					createViewRequest.setSourceEditPart(srcPart);
					createViewRequest.setTargetEditPart(targetPart);
					createViewRequest
							.setType(org.eclipse.gef.RequestConstants.REQ_CONNECTION_START);
					srcPart.getCommand(createViewRequest);
					createViewRequest
							.setType(org.eclipse.gef.RequestConstants.REQ_CONNECTION_END);
					Command createCommand = targetPart
							.getCommand(createViewRequest);
					if (createCommand != null) {
						compoundCommand.add(createCommand);
					}
				}
			}
		}

		dropRequest.setResult(allObjects);

		// Update all create objects based on the Artifact Manager content
		// Note: this will populate attr, method, and associations as needed.
		TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) getHost())
				.getEditingDomain();

		DiagramEditDomain domain = (DiagramEditDomain) getHost().getViewer()
				.getEditDomain();

		IResource res = (IResource) domain.getEditorPart().getEditorInput()
				.getAdapter(IResource.class);

		ITigerstripeModelProject project = (ITigerstripeModelProject) EclipsePlugin
				.getITigerstripeProjectFor(res.getProject());

		PostCreationModelUpdateCommand postCreateCommand = new PostCreationModelUpdateCommand(
				editingDomain, viewDescriptors, dropRequest.getObjects(),
				project);
		compoundCommand.add(new ICommandProxy(postCreateCommand));

		// Make sure everything is refreshed properly
		RefreshConnectionsRequest refreshRequest = new RefreshConnectionsRequest(
				allObjects);
		Command refreshCommand = getHost().getCommand(refreshRequest);

		// and re-arranged as needed
		ArrangeRequest arrangeRequest = new ArrangeRequest(
				RequestConstants.REQ_ARRANGE_DEFERRED);
		arrangeRequest.setViewAdaptersToArrange(allObjects);
		Command arrangeCommand = getHost().getCommand(arrangeRequest);

		compoundCommand.add(refreshCommand);
		compoundCommand.add(arrangeCommand);
		return compoundCommand;
	}

	@Override
	public Command getDropObjectsCommand(DropObjectsRequest dropRequest) {
		// Create a view request from the drop request and then forward getting
		// the command for that.

		List<ViewDescriptor> viewDescriptors = new ArrayList<ViewDescriptor>();
		Iterator iter = dropRequest.getObjects().iterator();

		if (dropRequest.getObjects().size() > 0
				&& dropRequest.getObjects().get(0) instanceof String)
			return getDropFileCommand(dropRequest);

		while (iter.hasNext()) {
			Object newObj = iter.next();
			IElementType type = ElementTypeMapper.mapToElementType(newObj);
			if (type != null) {
				if (type == ElementTypeMapper.Association_3001
						|| type == ElementTypeMapper.Dependency_3008) {
					CreateRelationshipRequest crRequest = new CreateRelationshipRequest(
							type);
					crRequest.setParameter("IAbstractArtifact", newObj);
					crRequest.setParameter(DRAGGED_ARTIFACT, new Boolean(true));
					CreateElementRequestAdapter adapter = new CreateElementRequestAdapter(
							crRequest);

					ConnectionViewAndElementDescriptor viewDescriptor = new ConnectionViewAndElementDescriptor(
							adapter, ((IHintedType) type).getSemanticHint(),
							((IGraphicalEditPart) getHost())
									.getDiagramPreferencesHint());
					viewDescriptors.add(viewDescriptor);
				} else if (type == ElementTypeMapper.AssociationClass_3010) {
					// first create the request for the link between the aEnd
					// and zEnd
					CreateRelationshipRequest crRequest = new CreateRelationshipRequest(
							type);
					crRequest.setParameter("IAbstractArtifact", newObj);
					crRequest.setParameter(DRAGGED_ARTIFACT, new Boolean(true));
					CreateElementRequestAdapter adapter = new CreateElementRequestAdapter(
							crRequest);
					ConnectionViewAndElementDescriptor viewDescriptor = new ConnectionViewAndElementDescriptor(
							adapter, ((IHintedType) type).getSemanticHint(),
							((IGraphicalEditPart) getHost())
									.getDiagramPreferencesHint());
					viewDescriptors.add(viewDescriptor);

					// then create the request for the AssociationClassClass
					// object
					IElementType elementType = ElementTypeMapper.AssociationClassClass_1009;
					CreateElementRequest crElemRequest = new CreateElementRequest(
							elementType);
					crElemRequest.setParameter("IAbstractArtifact", newObj);
					crElemRequest.setParameter(DRAGGED_ARTIFACT, new Boolean(
							true));
					CreateElementRequestAdapter elemAdapter = new CreateElementRequestAdapter(
							crElemRequest);
					ViewAndElementDescriptor elemViewDescriptor = new ViewAndElementDescriptor(
							elemAdapter, Node.class,
							((IHintedType) elementType).getSemanticHint(),
							((IGraphicalEditPart) getHost())
									.getDiagramPreferencesHint());
					viewDescriptors.add(elemViewDescriptor);
				} else {
					CreateElementRequest crRequest = new CreateElementRequest(
							type);
					crRequest.setParameter("IAbstractArtifact", newObj);
					crRequest.setParameter(DRAGGED_ARTIFACT, new Boolean(true));

					CreateElementRequestAdapter adapter = new CreateElementRequestAdapter(
							crRequest);

					ViewAndElementDescriptor viewDescriptor = new ViewAndElementDescriptor(
							adapter, Node.class, ((IHintedType) type)
									.getSemanticHint(),
							((IGraphicalEditPart) getHost())
									.getDiagramPreferencesHint());
					viewDescriptors.add(viewDescriptor);
				}
			}
		}
		return createViewsAndArrangeCommand(dropRequest, viewDescriptors);
	}

}
