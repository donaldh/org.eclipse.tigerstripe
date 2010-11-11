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
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewAndElementRequest.ConnectionViewAndElementDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest.ViewAndElementDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RefreshConnectionsRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.type.core.IHintedType;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.swt.dnd.DND;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IType;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.AbstractArtifactAdapter;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync.commands.PostCreationModelUpdateCommand;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.MapHelper;

public class ClassDiagramDragDropEditPolicy extends DiagramDragDropEditPolicy {

	public final static String DRAGGED_ARTIFACT = "draggedArtifact";

	@Override
	protected Command createViewsAndArrangeCommand(
			DropObjectsRequest dropRequest, List viewDescriptors) {

		List<Object> allObjects = new ArrayList<Object>(); // to contain all
		// final objects

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

		dropRequest.setRequiredDetail(DND.DROP_COPY);
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
				project, getHost());
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
		if (dropRequest.getObjects().size() > 0)
			if (dropRequest.getObjects().get(0) instanceof String) {
				return getDropFileCommand(dropRequest);
			} else if (dropRequest.getObjects().get(0) instanceof IAbstractArtifact) {
				return getDropArtifactsCommand(dropRequest);
			}

		return super.getDropObjectsCommand(dropRequest);
	}

	protected Command getDropArtifactsCommand(DropObjectsRequest dropRequest) {
		List<IAbstractArtifact> requestArtifacts = dropRequest.getObjects();

		EditPart mapEditPart = getHost();
		Diagram diagram = (Diagram) mapEditPart.getModel();
		Map map = (Map) diagram.getElement();

		List<IAbstractArtifact> artifactsToDrop = new ArrayList<IAbstractArtifact>();
		List<IAssociationArtifact> associations = new ArrayList<IAssociationArtifact>();
		for (IAbstractArtifact artifact : requestArtifacts) {
			if (!canDrop(artifact, map)) {
				return UnexecutableCommand.INSTANCE;
			}
			if (artifact instanceof IAssociationArtifact) {
				associations.add((IAssociationArtifact)artifact);
			} else {
				artifactsToDrop.add(artifact);
			}
		}
		processAssociations(map, associations, artifactsToDrop);
		
		dropRequest.setObjects(artifactsToDrop);
		List<ViewDescriptor> viewDescriptors = prepareViewDescriptors(artifactsToDrop);

		return createViewsAndArrangeCommand(dropRequest, viewDescriptors);
	}

	private void processAssociations(Map map,
			List<IAssociationArtifact> associations,
			List<IAbstractArtifact> artifactsToDrop) {
		MapHelper helper = new MapHelper(map);
		
		for (IAssociationArtifact assoc : associations) {
			boolean addAssoc = true;

			IType aEndType = assoc.getRelationshipAEnd().getType();
			if (helper.findElementFor(aEndType.getFullyQualifiedName()) == null) {
				IAbstractArtifact aEndArtifact = aEndType.getArtifact();
				if (!artifactsToDrop.contains(aEndArtifact)) {
					artifactsToDrop.add(aEndArtifact);					
				}				
				addAssoc = false;
			}

			IType zEndType = assoc.getRelationshipZEnd().getType();
			if (helper.findElementFor(zEndType.getFullyQualifiedName()) == null) {
				IAbstractArtifact zEndArtifact = zEndType.getArtifact();
				if (!artifactsToDrop.contains(zEndArtifact)) {
					artifactsToDrop.add(zEndArtifact);
				}
				addAssoc = false;
			}

			if (addAssoc) {
				artifactsToDrop.add(assoc);
			}
		}
	}

	private List<ViewDescriptor> prepareViewDescriptors(
			List<IAbstractArtifact> artifactsToDrop) {
		List<ViewDescriptor> viewDescriptors = new ArrayList<ViewDescriptor>();

		for (IAbstractArtifact artifact : artifactsToDrop) {

			IElementType type = ElementTypeMapper.mapToElementType(artifact);
			if (type != null) {
				if (type == ElementTypeMapper.Association_3001
						|| type == ElementTypeMapper.Dependency_3008) {
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
				} else if (type == ElementTypeMapper.AssociationClass_3010) {
					// first create the request for the link between the aEnd
					// and zEnd
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

					// then create the request for the AssociationClassClass
					// object
					IElementType elementType = ElementTypeMapper.AssociationClassClass_1009;
					CreateElementRequest crElemRequest = new CreateElementRequest(
							elementType);
					crElemRequest.setParameter("IAbstractArtifact", artifact);
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
					crRequest.setParameter("IAbstractArtifact", artifact);
					crRequest.setParameter(DRAGGED_ARTIFACT, new Boolean(true));

					CreateElementRequestAdapter adapter = new CreateElementRequestAdapter(
							crRequest);

					ViewAndElementDescriptor viewDescriptor = new ViewAndElementDescriptor(
							adapter, Node.class,
							((IHintedType) type).getSemanticHint(),
							((IGraphicalEditPart) getHost())
									.getDiagramPreferencesHint());
					viewDescriptors.add(viewDescriptor);
				}
			}
		}
		return viewDescriptors;
	}

	@Override
	protected Command getDropFileCommand(DropObjectsRequest dropRequest) {
		List<IAbstractArtifact> artifactsToDrop = new ArrayList<IAbstractArtifact>();

		EditPart mapEditPart = getHost();
		Diagram diagram = (Diagram) mapEditPart.getModel();
		Map map = (Map) diagram.getElement();
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
	private boolean canDrop(IAbstractArtifact artifact, Map map) {
		boolean result = true;
		if (artifact != null) {
			// make sure we don't add the same artifact twice on a diagram
			MapHelper helper = new MapHelper(map);

			QualifiedNamedElement element = helper.findElementFor(artifact
					.getFullyQualifiedName());

			if (artifact instanceof IRelationship) {
				// In the case of IRelationShip we need to check that both
				// ends are not null
				IRelationship rel = (IRelationship) artifact;
				if (rel.getRelationshipAEnd() != null
						&& rel.getRelationshipZEnd() != null) {
					// and on the diagram
					if (rel instanceof IDependencyArtifact) {
						String aEndFQN = rel.getRelationshipAEnd().getType()
								.getFullyQualifiedName();
						String zEndFQN = rel.getRelationshipZEnd().getType()
								.getFullyQualifiedName();

						result = (helper.findElementFor(aEndFQN) != null)
								&& (helper.findElementFor(zEndFQN) != null);
					}
					result = result && (element == null);
				} else {
					result = false;
				}
			} else {
				result = element == null;
			}
		} else {
			result = false;
		}
		return result;
	}
}
