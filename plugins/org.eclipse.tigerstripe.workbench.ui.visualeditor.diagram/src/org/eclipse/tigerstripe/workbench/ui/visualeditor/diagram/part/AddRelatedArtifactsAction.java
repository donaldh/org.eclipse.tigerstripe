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
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramEditDomain;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.api.artifacts.IArtifactManagerSession;
import org.eclipse.tigerstripe.api.artifacts.model.IAbstractArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IAssociationArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IAssociationClassArtifact;
import org.eclipse.tigerstripe.api.artifacts.model.IAssociationEnd;
import org.eclipse.tigerstripe.api.artifacts.model.IDependencyArtifact;
import org.eclipse.tigerstripe.api.external.TigerstripeException;
import org.eclipse.tigerstripe.api.external.model.artifacts.IArtifact;
import org.eclipse.tigerstripe.api.external.model.artifacts.IRelationship;
import org.eclipse.tigerstripe.api.external.model.artifacts.IRelationship.IRelationshipEnd;
import org.eclipse.tigerstripe.api.project.ITigerstripeProject;
import org.eclipse.tigerstripe.core.model.AssociationClassArtifact;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.dnd.ClassDiagramDragDropEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.dialogs.AddRelatedArtifactsDialog;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MapEditPart;
import org.eclipse.ui.IObjectActionDelegate;

public class AddRelatedArtifactsAction extends BaseDiagramPartAction implements
		IObjectActionDelegate {

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		super.selectionChanged(action, selection);
		// ensure that the action is enabled, no matter what
		boolean allArtifacts = false;
		try {
			allArtifacts = selectionIsAllArtifacts();
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
		action.setEnabled(allArtifacts);
	}

	public void run(IAction action) {
		IAbstractArtifact[] artifacts = getCorrespondingArtifacts();
		// IAbstractArtifact artifact = artifacts[0];
		Shell shell = EclipsePlugin.getActiveWorkbenchShell();
		Map map = getMap();
		List artifactsInMap = map.getArtifacts();
		List<String> namesOfArtifactsInMap = new ArrayList<String>();
		for (Object obj : artifactsInMap) {
			if (obj instanceof AbstractArtifact) {
				namesOfArtifactsInMap.add(((AbstractArtifact) obj)
						.getFullyQualifiedName());
			}
		}
		ITigerstripeProject tsProject = map
				.getCorrespondingITigerstripeProject();
		try {
			IArtifactManagerSession session = tsProject
					.getArtifactManagerSession();
			// a simple boolean flag that is set to true if a matching
			// relationship of any type is
			// found (used to differentiate between the case where no
			// relationships are available to
			// or from other objects for the selected artifact and the case
			// where relationships to
			// or from other objects exist but those objects are already in the
			// diagram
			boolean relationshipsExist = false;
			// assemble some lists. These lists are important for several
			// reasons. First, these
			// lists show how many related objects could be added to the
			// diagram, so they can be
			// used to set up our progress bar. Second, these lists are used to
			// construct a BitSet
			// that is passed into the AddRelatedArtifactsDialog, where it is
			// used to enable/disable
			// the appropriate checkboxes if there are no related objects of a
			// certain "type" that
			// can be added to the diagram (objects that are related by a
			// "Depends" relationship,
			// for example). Third, these lists will actually be used to add
			// objects to the diagram.

			// Handle extended Artifacts
			Set<IAbstractArtifact> extendedArtifacts = new HashSet<IAbstractArtifact>();
			for (IAbstractArtifact artifact : artifacts) {
				IAbstractArtifact extendedArtifact = artifact
						.getExtendedIArtifact();
				if (extendedArtifact != null
						&& !namesOfArtifactsInMap.contains(extendedArtifact
								.getFullyQualifiedName())) {
					extendedArtifacts.add(extendedArtifact);
					relationshipsExist = true;
				}
			}

			// Handle extending Artifacts
			Set<IAbstractArtifact> extendingArtifacts = new HashSet<IAbstractArtifact>();
			for (IAbstractArtifact artifact : artifacts) {
				IAbstractArtifact[] extendingArtArray = artifact
						.getExtendingArtifacts();
				for (IAbstractArtifact extendingArt : extendingArtArray) {
					if (!namesOfArtifactsInMap.contains(extendingArt
							.getFullyQualifiedName())) {
						relationshipsExist = true;
						extendingArtifacts.add(extendingArt);
					}
				}
			}

			// handle outgoing relationships
			Set<IAbstractArtifact> associatedArtifacts = new HashSet<IAbstractArtifact>();
			Set<IAbstractArtifact> dependentArtifacts = new HashSet<IAbstractArtifact>();
			for (IAbstractArtifact artifact : artifacts) {
				List<IRelationship> origRels = session
						.getOriginatingRelationshipForFQN(artifact
								.getFullyQualifiedName(), true);
				for (IRelationship relationship : origRels) {
					if (relationship instanceof IAssociationArtifact) {
						IAssociationEnd zEnd = (IAssociationEnd) ((IAssociationArtifact) relationship)
								.getZEnd();
						IArtifact associatedArt = zEnd.getIextType()
								.getIArtifact();
						// if an artifact of the same type isn't already in the
						// diagram, add it to the list
						// of associated artifacts that could be added
						if (!namesOfArtifactsInMap.contains(associatedArt
								.getFullyQualifiedName())) {
							relationshipsExist = true;
							associatedArtifacts
									.add((IAbstractArtifact) associatedArt);
						} else if (relationship instanceof IAssociationClassArtifact
								&& !namesOfArtifactsInMap
										.contains(((IAssociationClassArtifact) relationship)
												.getFullyQualifiedName())) {
							associatedArtifacts
									.add((IAbstractArtifact) relationship);
						}
					} else if (relationship instanceof IDependencyArtifact) {
						IRelationshipEnd zEnd = ((IDependencyArtifact) relationship)
								.getRelationshipZEnd();
						IArtifact dependentArt = zEnd.getType().getIArtifact();
						// if an artifact of the same type isn't already in the
						// diagram, add it to the list
						// of dependent artifacts that could be added
						if (!namesOfArtifactsInMap.contains(dependentArt
								.getFullyQualifiedName())) {
							relationshipsExist = true;
							dependentArtifacts
									.add((IAbstractArtifact) dependentArt);
						}
					}
				}
			}

			// Handling incoming relationships
			Set<IAbstractArtifact> associatingArtifacts = new HashSet<IAbstractArtifact>();
			Set<IAbstractArtifact> dependingArtifacts = new HashSet<IAbstractArtifact>();
			for (IAbstractArtifact artifact : artifacts) {
				List<IRelationship> termRels = session
						.getTerminatingRelationshipForFQN(artifact
								.getFullyQualifiedName(), true);
				for (IRelationship relationship : termRels) {
					if (relationship instanceof IAssociationArtifact) {
						IAssociationEnd aEnd = (IAssociationEnd) ((IAssociationArtifact) relationship)
								.getAEnd();
						IArtifact associatingArt = aEnd.getIextType()
								.getIArtifact();
						// if an artifact of the same type isn't already in the
						// diagram, add it to the list
						// of associating artifacts that could be added
						if (!namesOfArtifactsInMap.contains(associatingArt
								.getFullyQualifiedName())) {
							relationshipsExist = true;
							associatingArtifacts
									.add((IAbstractArtifact) associatingArt);
						} else if (relationship instanceof IAssociationClassArtifact
								&& !namesOfArtifactsInMap
										.contains(((IAssociationClassArtifact) relationship)
												.getFullyQualifiedName())) {
							associatingArtifacts
									.add((IAbstractArtifact) relationship);
						}
					} else if (relationship instanceof IDependencyArtifact) {
						IRelationshipEnd aEnd = ((IDependencyArtifact) relationship)
								.getRelationshipAEnd();
						IArtifact dependingArt = aEnd.getType().getIArtifact();
						// if an artifact of the same type isn't already in the
						// diagram, add it to the list
						// of depending artifacts that could be added
						if (!namesOfArtifactsInMap.contains(dependingArt
								.getFullyQualifiedName())) {
							relationshipsExist = true;
							dependingArtifacts
									.add((IAbstractArtifact) dependingArt);
						}
					}
				}
			}

			// Handling implemented Artifacts
			Set<IAbstractArtifact> implementedArtifacts = new HashSet<IAbstractArtifact>();
			for (IAbstractArtifact artifact : artifacts) {
				IAbstractArtifact[] implementedArtArray = artifact
						.getImplementedArtifacts();
				for (IAbstractArtifact implementedArt : implementedArtArray) {
					// if an artifact of the same type isn't already in the
					// diagram,
					// add it to the list
					// of implemented artifacts that could be added
					if (!namesOfArtifactsInMap.contains(implementedArt
							.getFullyQualifiedName())) {
						relationshipsExist = true;
						implementedArtifacts.add(implementedArt);
					}
				}
			}

			// Handling implementing Artifacts
			Set<IAbstractArtifact> implementingArtifacts = new HashSet<IAbstractArtifact>();
			for (IAbstractArtifact artifact : artifacts) {
				IArtifact[] implementingArtArray = ((IArtifact) artifact)
						.getImplementingIArtifacts();
				for (IArtifact implementingArt : implementingArtArray) {
					// if an artifact of the same type isn't already in the
					// diagram,
					// add it to the list
					// of implementing artifacts that could be added
					if (!namesOfArtifactsInMap.contains(implementingArt
							.getFullyQualifiedName())) {
						if (!relationshipsExist)
							relationshipsExist = true;
						implementingArtifacts
								.add((IAbstractArtifact) implementingArt);
					}
				}
			}

			// Handling referenced artifacts
			Set<IAbstractArtifact> referencedArtifacts = new HashSet<IAbstractArtifact>();
			for (IAbstractArtifact artifact : artifacts) {
				IArtifact[] referencedArtArray = ((IArtifact) artifact)
						.getReferencedIArtifacts();
				for (IArtifact referencedArt : referencedArtArray) {
					// if an artifact of the same type isn't already in the
					// diagram,
					// add it to the list
					// of implementing artifacts that could be added
					if (!namesOfArtifactsInMap.contains(referencedArt
							.getFullyQualifiedName())) {
						relationshipsExist = true;
						referencedArtifacts
								.add((IAbstractArtifact) referencedArt);
					}
				}
			}

			// Handling referencing artifacts
			Set<IAbstractArtifact> referencingArtifacts = new HashSet<IAbstractArtifact>();
			for (IAbstractArtifact artifact : artifacts) {
				IArtifact[] referencingArtArray = ((IArtifact) artifact)
						.getReferencingIArtifacts();
				for (IArtifact referencingArt : referencingArtArray) {
					// if an artifact of the same type isn't already in the
					// diagram,
					// add it to the list
					// of implementing artifacts that could be added
					if (!namesOfArtifactsInMap.contains(referencingArt
							.getFullyQualifiedName())) {
						relationshipsExist = true;
						referencingArtifacts
								.add((IAbstractArtifact) referencingArt);
					}
				}
			}

			// set up a mask to show which of these lists is of non-zero size
			// and which are not
			// (is used in the dialog to determine which types can be added to
			// the diagram and which
			// types cannot, either because they don't exist for the selected
			// object or because they
			// are already in the diagram)
			BitSet creationMask = new BitSet(10);
			if (extendedArtifacts.size() > 0)
				creationMask.set(0);
			if (extendingArtifacts.size() > 0)
				creationMask.set(1);
			if (associatedArtifacts.size() > 0)
				creationMask.set(2);
			if (associatingArtifacts.size() > 0)
				creationMask.set(3);
			if (dependentArtifacts.size() > 0)
				creationMask.set(4);
			if (dependingArtifacts.size() > 0)
				creationMask.set(5);
			if (implementedArtifacts.size() > 0)
				creationMask.set(6);
			if (implementingArtifacts.size() > 0)
				creationMask.set(7);
			if (referencedArtifacts.size() > 0)
				creationMask.set(8);
			if (referencingArtifacts.size() > 0)
				creationMask.set(9);
			// now use these flags in the constructor to the dialog (will
			// determine which checkboxes are
			// enabled and which are disabled in the dialog)
			Set<IAbstractArtifact> artifactsToAdd = new HashSet<IAbstractArtifact>();
			HashMap<AssociationClassArtifact, IAbstractArtifact[]> associationClassEndsMap = new HashMap<AssociationClassArtifact, IAbstractArtifact[]>();
			if (!creationMask.isEmpty()) {
				AddRelatedArtifactsDialog diag = new AddRelatedArtifactsDialog(
						shell, map, artifacts, creationMask);
				int returnStatus = diag.open();
				// if "OK" button was pressed, then continue with creation of
				// related artifacts (if any)
				// else do nothing (user cancelled the action using the "Cancel"
				// button)
				if (returnStatus == Window.OK) {
					// if here, then the OK button was pressed (so we are adding
					// related artifacts to
					// the diagram and creating links between those artifacts
					// and any other artifacts
					// that they are related to that already exist in the
					// diagram)
					if (diag.isExtendedSelected()) {
						updateArtifactsToAdd(extendedArtifacts, artifactsToAdd,
								associationClassEndsMap);
					}
					if (diag.isExtendingSelected()) {
						updateArtifactsToAdd(extendingArtifacts,
								artifactsToAdd, associationClassEndsMap);
					}
					if (diag.isAssociatedSelected()) {
						updateArtifactsToAdd(associatedArtifacts,
								artifactsToAdd, associationClassEndsMap);
					}
					if (diag.isAssociatingSelected()) {
						updateArtifactsToAdd(associatingArtifacts,
								artifactsToAdd, associationClassEndsMap);
					}
					if (diag.isDependentSelected()) {
						updateArtifactsToAdd(dependentArtifacts,
								artifactsToAdd, associationClassEndsMap);
					}
					if (diag.isDependingSelected()) {
						updateArtifactsToAdd(dependingArtifacts,
								artifactsToAdd, associationClassEndsMap);
					}
					if (diag.isImplementedSelected()) {
						updateArtifactsToAdd(implementedArtifacts,
								artifactsToAdd, associationClassEndsMap);
					}
					if (diag.isImplementingSelected()) {
						updateArtifactsToAdd(implementingArtifacts,
								artifactsToAdd, associationClassEndsMap);
					}
					if (diag.isReferencedSelected()) {
						updateArtifactsToAdd(referencedArtifacts,
								artifactsToAdd, associationClassEndsMap);
					}
					if (diag.isReferencingSelected()) {
						updateArtifactsToAdd(referencingArtifacts,
								artifactsToAdd, associationClassEndsMap);
					}
					// now, check the association class ends to make sure that
					// if any of them don't exist
					// they will also be added to the diagram (conversely, if
					// they both exist for any
					// of the association classes, add the association class to
					// the list of artifacts
					// to create so that it will be added to the diagram)
					for (AssociationClassArtifact assocClassArt : associationClassEndsMap
							.keySet()) {
						IAbstractArtifact[] endArray = associationClassEndsMap
								.get(assocClassArt);
						boolean addingEndpointToDiagram = false;
						for (IAbstractArtifact endArt : endArray) {
							if (!namesOfArtifactsInMap.contains(endArt
									.getFullyQualifiedName())
									&& !artifactsToAdd.contains(endArt)) {
								if (!addingEndpointToDiagram)
									addingEndpointToDiagram = true;
								artifactsToAdd.add(endArt);
							}
						}
						if (!addingEndpointToDiagram)
							artifactsToAdd.add(assocClassArt);
					}
					// now re-use the drag-and-drop code to add the appropriate
					// artifacts to the map
					// (just as if the user had dragged them over from the
					// Tigerstripe Explorer view)
					if (artifactsToAdd.size() > 0) {
						EditPart selectedEditPart = this.mySelectedElements[0];
						TransactionalEditingDomain editingDomain = ((IGraphicalEditPart) selectedEditPart)
								.getEditingDomain();
						MapEditPart mapEditPart = (MapEditPart) selectedEditPart
								.getParent();
						final IDiagramEditDomain diagramEditDomain = mapEditPart
								.getDiagramEditDomain();
						ClassDiagramDragDropEditPolicy dndEditPolicy = (ClassDiagramDragDropEditPolicy) mapEditPart
								.getEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE);
						DropObjectsRequest request = new DropObjectsRequest();
						List<IAbstractArtifact> artifactsToAddList = new ArrayList<IAbstractArtifact>();
						artifactsToAddList.addAll(artifactsToAdd);
						request.setObjects(artifactsToAddList);
						request.setAllowedDetail(DND.DROP_COPY);
						final Command cmd = dndEditPolicy
								.getDropObjectsCommand(request);
						if (cmd.canExecute()) {
							cmd.setLabel("Add Related Artifacts");
							// create a CommandProxy that we can use to execute
							// the command that we just created in the GMF
							// domain
							ICommand iCommand = new CommandProxy(cmd);
							try {
								// and execute it
								OperationHistoryFactory
										.getOperationHistory()
										.execute(iCommand,
												new NullProgressMonitor(), null);
							} catch (ExecutionException e) {
								EclipsePlugin.log(e);
							}
						}
					} else {
						String warningMessage = "";
						if (artifacts.length == 1) {
							warningMessage = "No additional related artifacts can be found for the selected artifact\n"
									+ "("
									+ artifacts[0].getFullyQualifiedName()
									+ ")";
						} else {
							String artList = "";
							for (IAbstractArtifact artifact : artifacts) {
								if (artList.length() != 0)
									artList += ", ";
								artList += artifact.getName();
							}
							warningMessage = "No additional related artifacts can be found for the selected artifacts\n"
									+ "(" + artList + ")";
						}
						MessageDialog.openWarning(shell,
								"No Related Artifacts Found", warningMessage);
					}
				}
			} else {
				// nothing can be added, so display a warning to the user and
				// return...
				String warningMessage = null;
				if (!relationshipsExist) {
					// If here, no relationships were found from/to other
					// objects in the model, so
					// there are no "related objects" to add.
					if (artifacts.length == 1)
						warningMessage = "No related artifacts can be found for the selected artifact\n"
								+ "("
								+ artifacts[0].getFullyQualifiedName()
								+ ")";
					else {
						String artList = "";
						for (IAbstractArtifact artifact : artifacts) {
							if (artList.length() != 0)
								artList += ", ";
							artList += artifact.getName();
						}
						warningMessage = "No related artifacts can be found for the selected artifacts\n"
								+ "(" + artList + ")";
					}
				} else {
					// If here, at least one other type of relationship was
					// found, but all of the
					// objects that are related to this object already exist in
					// the diagram, so there
					// are no "related objects" to add.
					if (artifacts.length == 1) {
						warningMessage = "No additional related artifacts can be found for the selected artifact\n"
								+ "("
								+ artifacts[0].getFullyQualifiedName()
								+ ")";
					} else {
						String artList = "";
						for (IAbstractArtifact artifact : artifacts) {
							if (artList.length() != 0)
								artList += ", ";
							artList += artifact.getName();
						}
						warningMessage = "No additional related artifacts can be found for the selected artifacts\n"
								+ "(" + artList + ")";
					}
				}
				MessageDialog.openWarning(shell, "No Related Artifacts Found",
						warningMessage);
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	// takes a list of artifacts and sorts them into either artifacts that can
	// be
	// added directly to the map or association class artifacts (which need to
	// be
	// handled separately, in case one or the other of their ends don't exist)
	public void updateArtifactsToAdd(
			Set<IAbstractArtifact> artifacts,
			Set<IAbstractArtifact> artifactsToAdd,
			HashMap<AssociationClassArtifact, IAbstractArtifact[]> associationClassEndsMap) {
		for (IAbstractArtifact artifact : artifacts) {
			if (artifact instanceof AssociationClassArtifact) {
				AssociationClassArtifact assocClassArt = (AssociationClassArtifact) artifact;
				IAbstractArtifact[] endArray = new IAbstractArtifact[] {
						(IAbstractArtifact) assocClassArt.getAEnd()
								.getIextType().getIArtifact(),
						(IAbstractArtifact) assocClassArt.getZEnd()
								.getIextType().getIArtifact() };
				associationClassEndsMap.put(assocClassArt, endArray);
			} else {
				artifactsToAdd.add(artifact);
			}
		}
	}

}
