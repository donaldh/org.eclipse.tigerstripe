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
package org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.sync;

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
import org.eclipse.tigerstripe.api.TigerstripeException;
import org.eclipse.tigerstripe.api.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.core.util.Util;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh.IEObjectRefresher;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.refresh.RefresherFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.helpers.MapHelper;

/**
 * This class can handle any synchronization tasks. It was introduced to avoid
 * duplication of synchronization code between ClosedDiagram synchronization and
 * open diagram synchronization
 * 
 * @author eric
 * 
 */
public class ClassDiagramSynchronizerUtils {

	public static void handleQualifiedNamedElementChanged(Diagram diagram,
			DiagramEditPart diagramEP, final Map map,
			final IAbstractArtifact artifact,
			TransactionalEditingDomain editingDomain,
			IDiagramEditDomain diagramEditDomain) throws TigerstripeException {
		List<AbstractArtifact> artifacts = map.getArtifacts();
		List<Association> associations = map.getAssociations();
		List<Dependency> dependencies = map.getDependencies();

		for (AbstractArtifact eArtifact : artifacts) {
			if (artifact.getFullyQualifiedName().equals(
					eArtifact.getFullyQualifiedName())) {
				updateEArtifact(diagram, diagramEP, editingDomain,
						diagramEditDomain, eArtifact, artifact);
			}
		}

		List<Association> associationsForRemoval = new ArrayList<Association>();
		for (Association eAssociation : associations) {
			if (artifact.getFullyQualifiedName().equals(
					eAssociation.getFullyQualifiedName())) {
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
		removeMarkedAssociations(editingDomain, map, associationsForRemoval);

		List<Dependency> dependenciesForRemoval = new ArrayList<Dependency>();
		for (Dependency eDependency : dependencies) {
			if (artifact.getFullyQualifiedName().equals(
					eDependency.getFullyQualifiedName())) {
				updateEArtifact(diagram, diagramEP, editingDomain,
						diagramEditDomain, eDependency, artifact);
				if (eDependency.getAEnd() == null
						|| eDependency.getZEnd() == null) {
					dependenciesForRemoval.add(eDependency);
				}
			}
		}

		// if dependencies have either end set to null, ie one of their ends
		// is not on the diagram, they should be removed from the EMF model
		// all together
		removeMarkedDependencies(editingDomain, map, dependenciesForRemoval);
	}

	protected static void updateEArtifact(Diagram diagram,
			DiagramEditPart diagramEP, TransactionalEditingDomain editDomain,
			IDiagramEditDomain diagramEditDomain,
			QualifiedNamedElement eElement, IAbstractArtifact iArtifact) {
		try {
			IEObjectRefresher refresher = RefresherFactory.getInstance()
					.getDefaultRefresher(editDomain, eElement);

			// if no refresher is found, return without an update
			if (refresher == null)
				return;

			// otherwise, get the command to use to update the artifact
			refresher.setInitialRefresh(true);
			Command cmd = refresher.refresh(eElement, iArtifact);

			BaseETAdapter.setIgnoreNotify(true);
			editDomain.getCommandStack().execute(cmd);
			editDomain.getCommandStack().flush();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		} finally {
			BaseETAdapter.setIgnoreNotify(false);
		}
		//
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

		try {
			BaseETAdapter.setIgnoreNotify(true);
			diagramEditDomain.getDiagramCommandStack().execute(cCom);
		} finally {
			BaseETAdapter.setIgnoreNotify(false);
		}
	}

	public static void handleQualifiedNamedElementRemoved(final Map map,
			final String fqn, TransactionalEditingDomain editingDomain,
			IDiagramEditDomain diagramEditDomain) throws TigerstripeException {

		MapHelper helper = new MapHelper(map);
		final QualifiedNamedElement element = helper.findElementFor(fqn);
		if (element != null) {
			Command cmd = new AbstractCommand() {

				@Override
				public boolean canExecute() {
					return true;
				}

				public void execute() {
					if (element instanceof AbstractArtifact)
						map.getArtifacts().remove(element);
					else if (element instanceof Association) {
						map.getAssociations().remove(element);
						if (element instanceof AssociationClass) {
							MapHelper helper = new MapHelper(map);
							AbstractArtifact aClassClass = helper
									.findAbstractArtifactFor(element
											.getFullyQualifiedName());
							if (aClassClass != null)
								map.getArtifacts().remove(aClassClass);
						}

					} else if (element instanceof Dependency)
						map.getDependencies().remove(element);
				}

				public void redo() {
					// TODO Auto-generated method stub

				}

				@Override
				public boolean canUndo() {
					return false;
				}
			};

			try {
				BaseETAdapter.setIgnoreNotify(true);
				editingDomain.getCommandStack().execute(cmd);
				editingDomain.getCommandStack().flush();
				diagramEditDomain.getDiagramCommandStack().flush();
			} finally {
				BaseETAdapter.setIgnoreNotify(false);
			}
			if (element instanceof AbstractArtifact
					|| element instanceof AssociationClass) {
				// Bug 526
				// Check that if an end to an assoc, assocClass or dep has been
				// removed
				// the corresponding assoc, assocClass or dep is actually
				// removed from
				// the
				// underlying model
				List<Association> associations = map.getAssociations();
				List<Dependency> dependencies = map.getDependencies();
				List<Association> associationsForRemoval = new ArrayList<Association>();
				for (Association eAssociation : associations) {
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
				// if associations have either end set to null, ie one of their
				// ends
				// is not on the diagram, they should be removed from the EMF
				// model
				// all together
				removeMarkedAssociations(editingDomain, map,
						associationsForRemoval);

				List<Dependency> dependenciesForRemoval = new ArrayList<Dependency>();
				for (Dependency eDependency : dependencies) {
					if (eDependency.getAEnd() == null
							|| eDependency.getZEnd() == null) {
						dependenciesForRemoval.add(eDependency);
					} else if (element.getFullyQualifiedName().equals(
							eDependency.getAEnd().getFullyQualifiedName())
							|| element.getFullyQualifiedName().equals(
									eDependency.getZEnd()
											.getFullyQualifiedName())) {
						dependenciesForRemoval.add(eDependency);
					}
				}

				// if dependencies have either end set to null, ie one of their
				// ends
				// is not on the diagram, they should be removed from the EMF
				// model
				// all together
				removeMarkedDependencies(editingDomain, map,
						dependenciesForRemoval);
			}
		}

	}

	/**
	 * 
	 * @param dependenciesForRemoval
	 */
	protected static void removeMarkedDependencies(
			TransactionalEditingDomain editingDomain, final Map map,
			final List<Dependency> dependenciesForRemoval) {

		if (dependenciesForRemoval.isEmpty())
			return;

		Command cmd = new AbstractCommand() {

			@Override
			public boolean canUndo() {
				return false;
			}

			public void execute() {
				for (Dependency eDep : dependenciesForRemoval) {
					// TigerstripeRuntime.logInfoMessage("Removing " +
					// eDep.getFullyQualifiedName());
					map.getDependencies().remove(eDep);
				}
			}

			public void redo() {
			}

			@Override
			public boolean canExecute() {
				return !dependenciesForRemoval.isEmpty();
			}

		};

		try {
			BaseETAdapter.setIgnoreNotify(true);
			editingDomain.getCommandStack().execute(cmd);
			editingDomain.getCommandStack().flush();
		} finally {
			BaseETAdapter.setIgnoreNotify(false);
		}
	}

	/**
	 * 
	 * @param dependenciesForRemoval
	 */
	protected static void removeMarkedAssociations(
			TransactionalEditingDomain editingDomain, final Map map,
			final List<Association> associationsForRemoval) {

		if (associationsForRemoval.isEmpty())
			return;
		Command cmd = new AbstractCommand() {

			@Override
			public boolean canUndo() {
				return false;
			}

			public void execute() {
				for (Association eAssoc : associationsForRemoval) {
					// TigerstripeRuntime.logInfoMessage("Removing " +
					// eAssoc.getFullyQualifiedName());
					map.getAssociations().remove(eAssoc);

					if (eAssoc instanceof AssociationClass) {
						MapHelper helper = new MapHelper(map);
						AbstractArtifact aClassClass = helper
								.findAbstractArtifactFor(eAssoc
										.getFullyQualifiedName());
						if (aClassClass != null)
							map.getArtifacts().remove(aClassClass);
					}
				}
			}

			public void redo() {
			}

			@Override
			public boolean canExecute() {
				return !associationsForRemoval.isEmpty();
			}

		};

		try {
			BaseETAdapter.setIgnoreNotify(true);
			editingDomain.getCommandStack().execute(cmd);
			editingDomain.getCommandStack().flush();
		} finally {
			BaseETAdapter.setIgnoreNotify(false);
		}
	}

	public static void handleQualifiedNamedElementRenamed(final Map map,
			final String fromFQN, final String toFQN,
			TransactionalEditingDomain editingDomain,
			IDiagramEditDomain diagramEditDomain) throws TigerstripeException {
		final MapHelper helper = new MapHelper(map);
		final QualifiedNamedElement element = helper.findElementFor(fromFQN);
		final String toName = Util.nameOf(toFQN);
		final String toPackage = Util.packageOf(toFQN);

		if (element != null) {
			Command cmd = new AbstractCommand() {

				@Override
				public boolean canExecute() {
					return true;
				}

				public void execute() {
					element.setName(toName);
					element.setPackage(toPackage);

					if (element instanceof AssociationClass) {// looking for
						// the
						// corresponding
						// AssociationClassClass
						QualifiedNamedElement classClass = helper
								.findElementFor(fromFQN);
						if (classClass != null) {
							classClass.setName(toName);
							classClass.setPackage(toPackage);
						}
					}
				}

				public void redo() {
				}

				@Override
				public boolean canUndo() {
					return false;
				}
			};

			try {
				BaseETAdapter.setIgnoreNotify(true);
				editingDomain.getCommandStack().execute(cmd);
				editingDomain.getCommandStack().flush();
				diagramEditDomain.getDiagramCommandStack().flush();
			} finally {
				BaseETAdapter.setIgnoreNotify(false);
			}
		}

	}
}
