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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramGraphicalViewer;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.emf.adaptation.etadapter.BaseETAdapter;
import org.eclipse.tigerstripe.workbench.model.IRelationship;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Association;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AssociationClassClass;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Dependency;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.QualifiedNamedElement;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.VisualeditorFactory;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.dialogs.ManageLinksDialog;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.util.VisualeditorRelationshipUtils;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class ManageLinksAction extends BaseDiagramPartAction implements
		IObjectActionDelegate {

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		super.selectionChanged(action, selection);
		// ensure that the action is enabled, no matter what
		try {
			action.setEnabled(mySelectedElements.length == 1
					&& selectionIsAllArtifacts()
					|| mySelectedElements.length == 0);
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
			action.setEnabled(false);
		}
	}

	public void run(IAction action) {
		Shell shell = EclipsePlugin.getActiveWorkbenchShell();
		IAbstractArtifact artifact = null;
		if (mySelectedElements.length != 0) {
			IAbstractArtifact[] artifacts = getCorrespondingArtifacts();
			artifact = artifacts[0];
		}
		Object[] returnObjects = null;
		Map map = null;
		if (mySelectedElements.length != 0) {
			// if here, selection was an artifact, so build list of
			// relationships
			// from/to that artifact to/from the other artifacts in the diagram
			EObject[] elements = getCorrespondingEObjects();
			map = getMap();
			try {
				ITigerstripeModelProject project = getCorrespondingTigerstripeProject();
				returnObjects = VisualeditorRelationshipUtils
						.getPossibleRelationshipsForArtifact(project, map,
								artifact);
			} catch (TigerstripeException e) {
				return;
			}
		} else {
			IWorkbenchPart wbp = getMyTargetWorkbenchPart();
			if (wbp instanceof TigerstripeDiagramEditor) {
				// else selection was the map itself, , so build list of
				// all of the relationships that can be build between any of
				// the artifacts in the diagram
				TigerstripeDiagramEditor tsd = (TigerstripeDiagramEditor) wbp;
				Diagram obj = (Diagram) tsd.getDiagramEditPart().getModel();
				map = (Map) obj.getElement();
				try {
					ITigerstripeModelProject project = getCorrespondingTigerstripeProject();
					returnObjects = VisualeditorRelationshipUtils
							.getPossibleRelationshipsForMap(project, map);
				} catch (TigerstripeException e) {
					return;
				}
			}
		}
		Set<IRelationship> possibleRelationships = null;
		HashMap<String, QualifiedNamedElement> associationsInMap = null;
		HashMap<String, QualifiedNamedElement> dependenciesInMap = null;
		HashMap<String, QualifiedNamedElement> nodesInMap = null;
		if (returnObjects != null && returnObjects.length == 4) {
			// extract the returned values from the object array
			possibleRelationships = (Set<IRelationship>) returnObjects[0];
			associationsInMap = (HashMap<String, QualifiedNamedElement>) returnObjects[1];
			dependenciesInMap = (HashMap<String, QualifiedNamedElement>) returnObjects[2];
			nodesInMap = (HashMap<String, QualifiedNamedElement>) returnObjects[3];
		}
		if (possibleRelationships != null && possibleRelationships.size() > 0) {
			// display the list of relationships in a dialog (allowing user to
			// pick which relationships should be shown/hidden)
			ManageLinksDialog diag = new ManageLinksDialog(shell,
					possibleRelationships, associationsInMap.keySet(),
					dependenciesInMap.keySet());
			if (diag.open() == Window.OK) {
				// now get the values that the user selected and determine which
				// links need
				// to be created/destroyed based on the selected values in that
				// list
				HashMap<String, IRelationship> selectedValues = diag
						.getSelection();
				List<IRelationship> relationshipsToCreate = new ArrayList<IRelationship>();
				Set<String> selectedNames = selectedValues.keySet();
				Set<String> associationNames = associationsInMap.keySet();
				Set<String> dependencyNames = dependenciesInMap.keySet();
				for (String name : selectedNames) {
					IRelationship relationship = selectedValues.get(name);
					if (!associationNames.contains(name)
							&& !dependencyNames.contains(name))
						relationshipsToCreate.add(relationship);
				}
				// look to see if there are any associations that are in the map
				// that are not in the
				// selection...if so, need to destroy them
				List<QualifiedNamedElement> associationsToDestroy = new ArrayList<QualifiedNamedElement>();
				for (String name : associationNames) {
					if (!selectedNames.contains(name))
						if (artifact != null) {
							String artifactFQN = artifact
									.getFullyQualifiedName();
							QualifiedNamedElement elem = associationsInMap
									.get(name);
							String aEndFQN = ((Association) elem).getAEnd()
									.getFullyQualifiedName();
							String zEndFQN = ((Association) elem).getZEnd()
									.getFullyQualifiedName();
							if (artifactFQN.equals(aEndFQN)
									|| artifactFQN.equals(zEndFQN))
								associationsToDestroy.add(associationsInMap
										.get(name));
						} else
							associationsToDestroy.add(associationsInMap
									.get(name));
				}
				// look to see if there are any dependencies that are in the map
				// that are not in the
				// selection...if so, need to destroy them
				List<QualifiedNamedElement> dependenciesToDestroy = new ArrayList<QualifiedNamedElement>();
				for (String name : dependencyNames) {
					if (!selectedNames.contains(name))
						if (artifact != null) {
							String artifactFQN = artifact
									.getFullyQualifiedName();
							QualifiedNamedElement elem = dependenciesInMap
									.get(name);
							String aEndFQN = ((Dependency) elem).getAEnd()
									.getFullyQualifiedName();
							String zEndFQN = ((Dependency) elem).getZEnd()
									.getFullyQualifiedName();
							if (artifactFQN.equals(aEndFQN)
									|| artifactFQN.equals(zEndFQN))
								dependenciesToDestroy.add(dependenciesInMap
										.get(name));
						} else
							dependenciesToDestroy.add(dependenciesInMap
									.get(name));
				}

				// set up command to update the map by adding the new
				// relationships
				// and
				// destroying the unwanted associations and dependencies
				Command cmd = new UpdateRelationshipsCommand(map, nodesInMap,
						relationshipsToCreate, associationsToDestroy,
						dependenciesToDestroy);
				TigerstripeDiagramEditor editor = null;
				if (mySelectedElements.length != 0
						&& mySelectedElements[0] != null) {
					DiagramGraphicalViewer viewer = (DiagramGraphicalViewer) mySelectedElements[0]
							.getParent().getViewer();
					DiagramEditDomain domain = (DiagramEditDomain) viewer
							.getEditDomain();
					editor = (TigerstripeDiagramEditor) domain.getEditorPart();
				} else {
					editor = (TigerstripeDiagramEditor) getMyTargetWorkbenchPart();
				}

				try {
					BaseETAdapter.setIgnoreNotify(true);
					TransactionalEditingDomain editingDomain = editor
							.getEditingDomain();
					editingDomain.getCommandStack().execute(cmd);
					editingDomain.getCommandStack().flush();
				} finally {
					BaseETAdapter.setIgnoreNotify(false);
				}
			}
		} else {
			// if here, there were no relationships that could be built, so
			// display a warning to the user
			String warningMessage = null;
			if (artifact != null) {
				warningMessage = "No relationships found to/from selected artifact\n"
						+ "(" + artifact.getFullyQualifiedName() + ")";
			} else {
				warningMessage = "No relationships found between artifacts in "
						+ "the current diagram";
			}
			MessageDialog.openWarning(shell, "No Relationship Found",
					warningMessage);
		}
	}

	/**
	 * inner class, this class is the command that is used to create new
	 * relationships (associations, association classes, or dependencies) in the
	 * map or to remove existing relationships from the map. The relationships
	 * to create or relationships to destroy are passed into this command using
	 * it's constructor. The creation/destruction of the EObjects that represent
	 * those relationships occurs in this object's "execute()" method.
	 * 
	 * @author tjmcs
	 * 
	 */
	private class UpdateRelationshipsCommand extends AbstractCommand {

		Map map;

		List<IRelationship> relationshipsToCreate;

		List<QualifiedNamedElement> associationsToDestroy;

		List<QualifiedNamedElement> dependenciesToDestroy;

		HashMap<String, QualifiedNamedElement> nodesInMap;

		List<QualifiedNamedElement> associationsAdded = new ArrayList<QualifiedNamedElement>();

		List<QualifiedNamedElement> dependenciesAdded = new ArrayList<QualifiedNamedElement>();

		public UpdateRelationshipsCommand(Map map,
				HashMap<String, QualifiedNamedElement> nodesInMap,
				List<IRelationship> relationshipsToCreate,
				List<QualifiedNamedElement> associationsToDestroy,
				List<QualifiedNamedElement> dependenciesToDestroy) {
			this.map = map;
			this.nodesInMap = nodesInMap;
			this.relationshipsToCreate = relationshipsToCreate;
			this.associationsToDestroy = associationsToDestroy;
			this.dependenciesToDestroy = dependenciesToDestroy;
		}

		@Override
		public boolean canExecute() {
			return true;
		}

		public void execute() {
			associationsAdded.clear();
			dependenciesAdded.clear();
			for (IRelationship relationship : relationshipsToCreate) {
				if (relationship instanceof IAssociationClassArtifact) {
					IAssociationClassArtifact assocClassArtifact = (IAssociationClassArtifact) relationship;
					AssociationClass assocClass = VisualeditorFactory.eINSTANCE
							.createAssociationClass();
					AssociationClassClass assocClassClass = VisualeditorFactory.eINSTANCE
							.createAssociationClassClass();
					assocClass.setAssociatedClass(assocClassClass);
					// it's critical that the assocClassClass instance be added
					// to the map before we try to
					// initialize the association class, otherwise the call to
					// initializeAssociationClass will
					// fail with a NullPointerException if an enumeration is
					// included as one (or more) of the
					// fields in the AssociationClass iArtifact
					map.getArtifacts().add(assocClassClass);
					VisualeditorRelationshipUtils.initializeAssociationClass(
							assocClass, assocClassArtifact, nodesInMap);
					map.getAssociations().add(assocClass);
					associationsAdded.add(assocClass);
				} else if (relationship instanceof IAssociationArtifact) {
					IAssociationArtifact assocArtifact = (IAssociationArtifact) relationship;
					Association assoc = VisualeditorFactory.eINSTANCE
							.createAssociation();
					VisualeditorRelationshipUtils.initializeAssociation(assoc,
							assocArtifact, nodesInMap);
					map.getAssociations().add(assoc);
					associationsAdded.add(assoc);
				} else if (relationship instanceof IDependencyArtifact) {
					IDependencyArtifact depArtifact = (IDependencyArtifact) relationship;
					Dependency dependency = VisualeditorFactory.eINSTANCE
							.createDependency();
					VisualeditorRelationshipUtils.initializeDependency(
							dependency, depArtifact, nodesInMap);
					map.getDependencies().add(dependency);
					dependenciesAdded.add(dependency);
				}
			}
			map.getAssociations().removeAll(associationsToDestroy);
			map.getDependencies().removeAll(dependenciesToDestroy);
		}

		public void redo() {
			map.getAssociations().addAll(associationsAdded);
			map.getDependencies().addAll(dependenciesAdded);
			map.getAssociations().removeAll(associationsToDestroy);
			map.getDependencies().removeAll(dependenciesToDestroy);
		}

		@Override
		public void undo() {
			map.getAssociations().removeAll(associationsAdded);
			map.getDependencies().removeAll(dependenciesAdded);
			map.getAssociations().addAll(associationsToDestroy);
			map.getDependencies().addAll(dependenciesToDestroy);
		}

		@Override
		public boolean canUndo() {
			return true;
		}

	}

}
