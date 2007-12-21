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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IField;
import org.eclipse.tigerstripe.api.artifacts.model.ILabel;
import org.eclipse.tigerstripe.api.artifacts.model.IMethod;
import org.eclipse.tigerstripe.api.artifacts.model.IModelComponent;
import org.eclipse.tigerstripe.api.artifacts.updater.IModelChangeRequest;
import org.eclipse.tigerstripe.api.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.api.artifacts.updater.request.IArtifactDeleteRequest;
import org.eclipse.tigerstripe.api.artifacts.updater.request.IArtifactRemoveFeatureRequest;
import org.eclipse.tigerstripe.api.artifacts.updater.request.IArtifactSetFeatureRequest;
import org.eclipse.tigerstripe.api.artifacts.updater.request.IAttributeRemoveRequest;
import org.eclipse.tigerstripe.api.artifacts.updater.request.ILabelRemoveRequest;
import org.eclipse.tigerstripe.api.artifacts.updater.request.IMethodRemoveRequest;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.model.artifacts.IRelationship;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.builder.WorkspaceListener;
import org.eclipse.tigerstripe.workbench.ui.eclipse.views.explorerview.TSExplorerUtils;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AbstractArtifactExtendsEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.AbstractArtifactImplementsEditPart;
import org.eclipse.ui.IObjectActionDelegate;

public class TSRemoveFromModelAction extends BaseDiagramPartAction implements
		IObjectActionDelegate {

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		super.selectionChanged(action, selection);

		if (allWriteableRelationships()) {
			action.setEnabled(true);
			return;
		}

		if (mySelectedElements.length != 0) {
			IAbstractArtifact[] artifacts = getCorrespondingArtifacts();

			boolean allReadable = true;
			for (IAbstractArtifact artifact : artifacts) {
				// this action is not applicable to artifact coming from
				// dependency
				// modules.
				if (artifact != null) {
					if (artifact.isReadonly())
						allReadable = false;
				}
			}
			action.setEnabled(allReadable);
		}
	}

	/**
	 * Returns true if all the selected elements correspond to writeable Extends
	 * relationships.
	 * 
	 * @return
	 */
	private boolean allWriteableRelationships() {
		boolean result = true;

		for (EditPart part : mySelectedElements) {
			if (part instanceof AbstractArtifactExtendsEditPart) {
				continue;
			} else if (part instanceof AbstractArtifactImplementsEditPart) {
				continue;
			} else {
				result = false;
				break;
			}
		}
		return result;
	}

	private void removeExtendsRelationshipsFromModel(IAction action)
			throws TigerstripeException {

		if (mySelectedElements.length == 0)
			return;

		ITigerstripeProject project = getCorrespondingTigerstripeProject();
		IModelUpdater updater = project.getArtifactManagerSession()
				.getIModelUpdater();

		String message = "this relationship";
		if (mySelectedElements.length > 1) {
			message = "these relationships";
		}
		if (MessageDialog
				.openConfirm(
						getShell(),
						"Delete from Model",
						"Do you really want to delete "
								+ message
								+ " from the model?\n(This operation cannot be undone)")) {
			for (EditPart part : mySelectedElements) {
				if (part instanceof AbstractArtifactExtendsEditPart) {
					AbstractArtifactExtendsEditPart aaeep = (AbstractArtifactExtendsEditPart) part;
					Edge edge = (Edge) aaeep.getModel();
					Node sourceNode = (Node) edge.getSource();
					AbstractArtifact art = (AbstractArtifact) sourceNode
							.getElement();
					IArtifactSetFeatureRequest req = (IArtifactSetFeatureRequest) updater
							.getRequestFactory().makeRequest(
									IArtifactSetFeatureRequest.class.getName());
					req.setArtifactFQN(art.getFullyQualifiedName());
					req
							.setFeatureId(IArtifactSetFeatureRequest.EXTENDS_FEATURE);
					req.setFeatureValue("");
					updater.handleChangeRequest(req);
				} else if (part instanceof AbstractArtifactImplementsEditPart) {
					AbstractArtifactImplementsEditPart aaiep = (AbstractArtifactImplementsEditPart) part;
					Edge edge = (Edge) aaiep.getModel();
					Node sourceNode = (Node) edge.getSource();
					Node targetNode = (Node) edge.getTarget();
					AbstractArtifact art = (AbstractArtifact) sourceNode
							.getElement();
					AbstractArtifact targetArt = (AbstractArtifact) targetNode
							.getElement();
					IArtifactRemoveFeatureRequest req = (IArtifactRemoveFeatureRequest) updater
							.getRequestFactory().makeRequest(
									IArtifactRemoveFeatureRequest.class
											.getName());
					req.setArtifactFQN(art.getFullyQualifiedName());
					req
							.setFeatureId(IArtifactRemoveFeatureRequest.IMPLEMENTS_FEATURE);
					req.setFeatureValue(targetArt.getFullyQualifiedName());
					updater.handleChangeRequest(req);
				}
			}
		}
	}

	public void run(IAction action) {

		if (allWriteableRelationships()) {
			try {
				removeExtendsRelationshipsFromModel(action);
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
			return;
		}

		IModelComponent[] components = getCorrespondingModelComponents();

		if (components.length != 0) {

			String message = "this element";
			if (components.length > 1) {
				message = "these elements";
			}

			if (MessageDialog
					.openConfirm(
							getShell(),
							"Delete from Model",
							"Do you really want to delete "
									+ message
									+ " from the model?\n(This operation cannot be undone)")) {
				try {
					ITigerstripeProject project = getCorrespondingTigerstripeProject();

					Set<IRelationship> relationshipsToCascadeDelete = new HashSet<IRelationship>();
					for (IModelComponent component : components) {
						IModelUpdater updater = project
								.getArtifactManagerSession().getIModelUpdater();
						IModelChangeRequest request = null;
						if (component instanceof IAbstractArtifact) {
							request = updater.getRequestFactory().makeRequest(
									IArtifactDeleteRequest.class.getName());
							((IArtifactDeleteRequest) request)
									.setArtifactName(((IAbstractArtifact) component)
											.getName());
							((IArtifactDeleteRequest) request)
									.setArtifactPackage(((IAbstractArtifact) component)
											.getPackage());

							// Compute list of Relationship to cascade delete
							// This should be controled by a preference in the
							// GUI
							if (true) {
								relationshipsToCascadeDelete
										.addAll(project
												.getArtifactManagerSession()
												.getOriginatingRelationshipForFQN(
														((IAbstractArtifact) component)
																.getFullyQualifiedName(),
														false));
								relationshipsToCascadeDelete
										.addAll(project
												.getArtifactManagerSession()
												.getTerminatingRelationshipForFQN(
														((IAbstractArtifact) component)
																.getFullyQualifiedName(),
														false));
							}

						} else if (component instanceof IField) {
							IField field = (IField) component;
							IAttributeRemoveRequest aReq = (IAttributeRemoveRequest) updater
									.getRequestFactory().makeRequest(
											IAttributeRemoveRequest.class
													.getName());
							aReq.setArtifactFQN(field.getContainingArtifact()
									.getFullyQualifiedName());
							aReq.setAttributeName(field.getName());
							aReq.setSource(this);
							request = aReq;
						} else if (component instanceof IMethod) {
							IMethod method = (IMethod) component;
							IMethodRemoveRequest aReq = (IMethodRemoveRequest) updater
									.getRequestFactory().makeRequest(
											IMethodRemoveRequest.class
													.getName());
							aReq.setArtifactFQN(method.getContainingArtifact()
									.getFullyQualifiedName());
							aReq.setMethodName(method.getName());
							aReq.setSource(this);
							request = aReq;
						} else if (component instanceof ILabel) {
							ILabel label = (ILabel) component;
							ILabelRemoveRequest aReq = (ILabelRemoveRequest) updater
									.getRequestFactory()
									.makeRequest(
											ILabelRemoveRequest.class.getName());
							aReq.setArtifactFQN(label.getContainingArtifact()
									.getFullyQualifiedName());
							aReq.setLabelName(label.getName());
							aReq.setSource(this);
							request = aReq;
						}

						if (request == null) {
							IStatus st = new Status(IStatus.ERROR,
									TigerstripeDiagramEditorPlugin.ID, 222,
									"Can't remove " + component
											+ " from the model", null);
							EclipsePlugin.log(st);
						} else {
							updater.handleChangeRequest(request);
						}

						if (component instanceof IAbstractArtifact) {
							// An uglyness where the Art Mgr doesn't delete the
							// corresponding
							// POJO when an artifact is removed from the Art
							// Mgr.
							IResource res = TSExplorerUtils
									.getIResourceForArtifact(((IAbstractArtifact) component));
							if (res != null) {
								try {
									res.delete(true, new NullProgressMonitor());
								} catch (CoreException e) {
									EclipsePlugin.log(e);
								}
							}
						}

						handleCascadeDelete(relationshipsToCascadeDelete,
								updater);
					}
				} catch (TigerstripeException e) {
					EclipsePlugin.log(e);
				}
			}
		}
	}

	private void handleCascadeDelete(Set<IRelationship> toDeletes,
			IModelUpdater updater) throws TigerstripeException {
		WorkspaceListener.handleRelationshipsToCascadeDelete(toDeletes,
				updater, new NullProgressMonitor());
	}
}
