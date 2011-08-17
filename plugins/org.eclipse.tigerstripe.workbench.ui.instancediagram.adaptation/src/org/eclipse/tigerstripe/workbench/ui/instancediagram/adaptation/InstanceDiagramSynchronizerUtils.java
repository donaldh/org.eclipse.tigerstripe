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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.requests.ArrangeRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RefreshConnectionsRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.RequestConstants;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.core.util.Util;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.commands.IEObjectRefresher;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.helpers.InstanceDiagramMapHelper;

/**
 * This class can handle any synchronization tasks. It was introduced to avoid
 * duplication of synchronization code between ClosedDiagram synchronization and
 * open diagram synchronization
 * 
 * @author eric
 * @since Bug 936
 */
public class InstanceDiagramSynchronizerUtils {

	public static void handleArtifactChanged(Diagram diagram,
			DiagramEditPart diagramEP, final InstanceMap map,
			final IAbstractArtifact artifact,
			TransactionalEditingDomain editingDomain,
			IDiagramEditDomain diagramEditDomain) throws TigerstripeException {

		List<ClassInstance> classInstances = map.getClassInstances();
		List<AssociationInstance> associations = map.getAssociationInstances();
		IArtifactManagerSession artifactMgrSession = null;
		try {
			artifactMgrSession = map.getCorrespondingITigerstripeProject()
					.getArtifactManagerSession();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
			return;
		}

		for (ClassInstance eInstance : classInstances) {
			String fqn = eInstance.getFullyQualifiedName();
			IAbstractArtifact eArtifact = (IAbstractArtifact) artifactMgrSession
					.getArtifactByFullyQualifiedName(fqn);
			if (instanceIsRelated(eArtifact, artifact)) {
				updateEArtifact(diagram, diagramEP, editingDomain,
						diagramEditDomain, eInstance, artifact);
			}
		}

		List<AssociationInstance> associationsForRemoval = new ArrayList<AssociationInstance>();
		for (AssociationInstance eAssociation : associations) {
			if (artifact.getFullyQualifiedName().equals(
					eAssociation.getFullyQualifiedName())) {
				if (!(artifact instanceof IAssociationClassArtifact))
					// skip association class updates, those are handled as
					// class instances
					// in the instance diagram so they've already been
					// updated...
					updateEArtifact(diagram, diagramEP, editingDomain,
							diagramEditDomain, eAssociation, artifact);
				if (eAssociation.getAEnd() == null
						|| eAssociation.getZEnd() == null) {
					associationsForRemoval.add(eAssociation);
				}
			}
		}
		// if associations have either end set to null, ie one of their ends
		// is not on the diagram, they should be removed from the EMF model
		// all together
		removeMarkedAssociations(editingDomain, diagramEditDomain, map,
				associationsForRemoval);
	}

	private static boolean instanceIsRelated(IAbstractArtifact eArtifact,
			IAbstractArtifact iArtifact) {
		IAbstractArtifact localArtifact = eArtifact;
		String iArtifactType = iArtifact.getFullyQualifiedName();
		
		// Changed due to Bugzilla 319758: NPE during project import
		while (localArtifact != null) {
			if (localArtifact.getFullyQualifiedName().equals(iArtifactType))
				return true;
			
			localArtifact = localArtifact.getExtendedArtifact();
		}
		
		return false;
	}

	protected static void updateEArtifact(Diagram diagram,
			DiagramEditPart diagramEP, TransactionalEditingDomain editDomain,
			IDiagramEditDomain diagramEditDomain, Instance eElement,
			IAbstractArtifact iArtifact) {
		try {
			IEObjectRefresher refresher = InstanceDiagramRefresherFactory
					.getInstance().getDefaultRefresher(
							editDomain,
							eElement,
							((InstanceMap) diagram.getElement())
									.getCorrespondingITigerstripeProject()
									.getArtifactManagerSession());

			// if no refresher is found, return without an update
			if (refresher == null)
				return;

			// otherwise, get the command to use to update the artifact
			refresher.setInitialRefresh(true);
			Command cmd = refresher.refresh(eElement, iArtifact);

			editDomain.getCommandStack().execute(cmd);
			editDomain.getCommandStack().flush();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		CompoundCommand cCom = new CompoundCommand();

		List<Node> children = diagram.getChildren();
		Node targetChild = null;
		for (Node child : children) {
			if (child.getElement() == eElement) {
				targetChild = child;
			}
		}

		// PostCreationModelUpdateCommand cmd = new
		// PostCreationModelUpdateCommand(
		// editingDomain, eArtifact, iArtifact, getTSProject());
		// cCom.add(new ICommandProxy(cmd));
		RefreshConnectionsRequest refreshRequest = new RefreshConnectionsRequest(
				children);
		org.eclipse.gef.commands.Command refreshCommand = diagramEP
				.getCommand(refreshRequest);
		cCom.chain(refreshCommand);
		//
		//
		// and re-arranged as needed
		ArrangeRequest arrangeRequest = new ArrangeRequest(
				RequestConstants.REQ_ARRANGE_DEFERRED);
		arrangeRequest.setViewAdaptersToArrange(Collections
				.singletonList(targetChild));
		org.eclipse.gef.commands.Command arrangeCommand = diagramEP
				.getCommand(arrangeRequest);
		cCom.chain(arrangeCommand);

		diagramEditDomain.getDiagramCommandStack().execute(cCom);
	}

	public static void handleArtifactRemoved(final InstanceMap map,
			final String fqn, TransactionalEditingDomain editingDomain,
			IDiagramEditDomain diagramEditDomain) throws TigerstripeException {
		InstanceDiagramMapHelper helper = new InstanceDiagramMapHelper(map);
		final List<Instance> elements = helper.findElementFor(fqn);
		if (elements.size() > 0) {
			for (final Instance element : elements) {
				Command cmd = new AbstractCommand() {

					@Override
					public boolean canExecute() {
						return true;
					}

					public void execute() {
						if (element instanceof ClassInstance)
							map.getClassInstances().remove(element);
						else if (element instanceof AssociationInstance)
							map.getAssociationInstances().remove(element);
					}

					public void redo() {
						// TODO Auto-generated method stub

					}

					@Override
					public boolean canUndo() {
						return false;
					}
				};

				editingDomain.getCommandStack().execute(cmd);
				editingDomain.getCommandStack().flush();
				diagramEditDomain.getDiagramCommandStack().flush();
				if (element instanceof ClassInstance) {
					// Bug 526
					// Check that if an end to an assoc, assocClass or dep has
					// been
					// removed the corresponding assoc, assocClass or dep is
					// actually
					// removed from the underlying model
					List<AssociationInstance> associations = map
							.getAssociationInstances();
					List<AssociationInstance> associationsForRemoval = new ArrayList<AssociationInstance>();
					for (AssociationInstance eAssociation : associations) {
						if (eAssociation.getAEnd() == null
								|| eAssociation.getZEnd() == null) {
							associationsForRemoval.add(eAssociation);
						} else if (element.getFullyQualifiedName().equals(
								eAssociation.getAEnd().getFullyQualifiedName())
								|| element.getFullyQualifiedName().equals(
										eAssociation.getZEnd()
												.getFullyQualifiedName())) {
							associationsForRemoval.add(eAssociation);
						}
					}
					removeMarkedAssociations(editingDomain, diagramEditDomain,
							map, associationsForRemoval);
				}
			}
		}
	}

	/**
	 * 
	 * @param editingDomain
	 * @param map
	 * @param associationsForRemoval
	 */
	protected static void removeMarkedAssociations(
			TransactionalEditingDomain editingDomain,
			IDiagramEditDomain diagramEditDomain, final InstanceMap map,
			final List<AssociationInstance> associationsForRemoval) {
		if (associationsForRemoval.isEmpty())
			return;
		Command cmd = new AbstractCommand() {

			@Override
			public boolean canUndo() {
				return false;
			}

			public void execute() {
				for (AssociationInstance eAssoc : associationsForRemoval) {
					map.getAssociationInstances().remove(eAssoc);
				}
			}

			public void redo() {
			}

			@Override
			public boolean canExecute() {
				return !associationsForRemoval.isEmpty();
			}

		};

		editingDomain.getCommandStack().execute(cmd);
		editingDomain.getCommandStack().flush();
		diagramEditDomain.getDiagramCommandStack().flush();
	}

	public static void handleArtifactRenamed(final InstanceMap map,
			final String fromFQN, final String toFQN,
			TransactionalEditingDomain editingDomain,
			IDiagramEditDomain diagramEditDomain) throws TigerstripeException {

		final InstanceDiagramMapHelper helper = new InstanceDiagramMapHelper(
				map);
		final List<Instance> elements = helper.findElementFor(fromFQN);
		final String toName = Util.nameOf(toFQN);
		final String toPackage = Util.packageOf(toFQN);

		if (elements.size() > 0) {
			for (final Instance element : elements) {
				Command cmd = new AbstractCommand() {

					@Override
					public boolean canExecute() {
						return true;
					}

					public void execute() {
						element.setName(toName);
						element.setPackage(toPackage);
					}

					public void redo() {
						// TODO Auto-generated method stub

					}

					@Override
					public boolean canUndo() {
						return false;
					}
				};

				editingDomain.getCommandStack().execute(cmd);
				editingDomain.getCommandStack().flush();
				diagramEditDomain.getDiagramCommandStack().flush();
			}
		}
	}
}
