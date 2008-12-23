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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.dnd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
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
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.commands.PostCreationModelUpdateCommand;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.helpers.InstanceDiagramMapHelper;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.AbstractArtifactAdapter;

public class InstanceDiagramDragDropEditPolicy extends
		DiagramDragDropEditPolicy {

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

				InstanceDiagramMapHelper helper = new InstanceDiagramMapHelper(
						(InstanceMap) ((Diagram) getHost().getModel())
								.getElement());
				EObject aEnd = helper.findClassInstanceFor(aEndType);
				EObject zEnd = helper.findClassInstanceFor(zEndType);

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

		ITigerstripeModelProject project = (ITigerstripeModelProject) res
				.getProject().getAdapter(ITigerstripeModelProject.class);

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

	protected Command getDropArtifactsCommand(DropObjectsRequest dropRequest) {

		EditPart mapEditPart = getHost();
		Diagram diagram = (Diagram) mapEditPart.getModel();
		InstanceMap map = (InstanceMap) diagram.getElement();

		List<ViewDescriptor> viewDescriptors = new ArrayList<ViewDescriptor>();
		List<IAbstractArtifact> artifactsToDrop = dropRequest.getObjects();

		for (IAbstractArtifact artifact : artifactsToDrop) {

			if (!canDrop(artifact, map)) {
				return UnexecutableCommand.INSTANCE;
			}

			IElementType type = InstanceElementTypeMapper
					.mapToElementType(artifact);
			if (type != null) {
				if (type == InstanceElementTypeMapper.AssociationInstance_3001) {
					CreateRelationshipRequest crRequest = new CreateRelationshipRequest(
							type);
					crRequest.setParameter("IAbstractArtifact", artifact);
					crRequest.setParameter(DRAGGED_ARTIFACT, new Boolean(true));
					CreateElementRequestAdapter adapter = new CreateElementRequestAdapter(
							crRequest);

					ConnectionViewAndElementDescriptor viewDescriptor = new ConnectionViewAndElementDescriptor(
							adapter, ((IHintedType) type).getSemanticHint(),
							((IGraphicalEditPart) getHost())
									.getDiagramPreferencesHint());
					viewDescriptors.add(viewDescriptor);
				} else {
					CreateElementRequest crRequest = new CreateElementRequest(
							type);
					crRequest.setParameter("IAbstractArtifact", artifact);
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

	@Override
	public Command getDropObjectsCommand(DropObjectsRequest dropRequest) {
		if (dropRequest.getObjects().size() > 0)
			if (dropRequest.getObjects().get(0) instanceof String) {
				return getDropFileCommand(dropRequest);
			} else if (dropRequest.getObjects().get(0) instanceof IAbstractArtifact) {
				return getDropArtifactsCommand(dropRequest);
			}

		return super.getDropObjectsCommand(dropRequest);
	}

	@Override
	protected Command getDropFileCommand(DropObjectsRequest dropRequest) {
		List<IAbstractArtifact> artifactsToDrop = new ArrayList<IAbstractArtifact>();

		EditPart mapEditPart = getHost();
		Diagram diagram = (Diagram) mapEditPart.getModel();
		InstanceMap map = (InstanceMap) diagram.getElement();
		ITigerstripeModelProject tsProject = map
				.getCorrespondingITigerstripeProject();

		if (tsProject == null)
			return UnexecutableCommand.INSTANCE;

		// Sort out the artifacts being dropped here
		for (Object obj : dropRequest.getObjects()) {
			if (obj instanceof String) {
				String filePath = (String) obj;
				IPath fPath = new Path(filePath);
				IFile[] files = ResourcesPlugin.getWorkspace().getRoot()
						.findFilesForLocation(fPath);
				for (IFile file : files) {
					IAbstractArtifact art = AbstractArtifactAdapter
							.adaptWithin(file, tsProject);
					if (art != null && canDrop(art, map)) {
						artifactsToDrop.add(art);
					} else {
						// if one is not adaptable, no point in going further
						// that means
						// "something else" is being dragged that is not an
						// artifact or if it is, it is
						// not in diagram scope (Following project path).
						return UnexecutableCommand.INSTANCE;
					}
				}
			}
		}

		// At this stage, we know for sure that all artifacts in the
		// artifactsToDrop List
		// can indeed be safely dropped. We now need to map them to the
		// corresponding types
		// and create the final command.
		dropRequest.setObjects(artifactsToDrop);
		return getDropArtifactsCommand(dropRequest);
	}

	/**
	 * Determines if the given artifact can be dropped on this Map.
	 * 
	 * @param artifact
	 * @return
	 */
	private boolean canDrop(IAbstractArtifact artifact, InstanceMap map) {
		boolean result = true;
		if (artifact != null) {
			// disable drag-and-drop of Associations, AssociationClasses,
			// and Dependencies onto an instance diagram (these are added using
			// the "handles" instead so that the source and target will be
			// known)
			if (artifact instanceof IRelationship)
				return false;

			// disable drag-and-drop of Enumerations
			if (artifact instanceof IEnumArtifact)
				return false;

			// if is an abstract artifact, then return false (cannot add
			// abstract artifacts to an instance diagram (tjmcs, 04-Apr-2007;
			// disabled this check in favor of a dialog box describing the error
			// per Cisco's request)
			// if (artifact.isAbstract()) return false;

			InstanceDiagramMapHelper helper = new InstanceDiagramMapHelper(map);

			List<Instance> instances = helper.findElementFor(artifact
					.getFullyQualifiedName());

			// if (artifact instanceof IRelationship) {
			// // In the case of IRelationShip we need to check that both
			// // ends are on the diagram
			// IRelationship rel = (IRelationship) artifact;
			// if (rel.getRelationshipAEnd() != null
			// && rel.getRelationshipZEnd() != null) {
			// String aEndFQN = rel.getRelationshipAEnd().getType()
			// .getFullyQualifiedName();
			// String zEndFQN = rel.getRelationshipZEnd().getType()
			// .getFullyQualifiedName();
			//
			// result = (instance == null)
			// && (helper.findElementFor(aEndFQN) != null)
			// && (helper.findElementFor(zEndFQN) != null);
			// } else {
			// result = false;
			// }
			// } else {
			// // make sure we don't add the same artifact twice on a
			// diagram
			// // without having a name for each instance
			// for (Instance instance : instances) {
			// // if there is an instance and it doesn't have a name, return
			// false
			// // (in that case, would have two instances without a name in
			// the diagram)
			// if (instance.getArtifactName() == null)
			// return false;
			// }
			// result = true;
			// }
		} else {
			result = false;
		}
		return result;
	}
}
