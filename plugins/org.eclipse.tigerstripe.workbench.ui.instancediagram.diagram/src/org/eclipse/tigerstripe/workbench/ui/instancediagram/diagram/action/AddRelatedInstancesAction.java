/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.dnd.DND;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationClassArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship.IRelationshipEnd;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.dnd.InstanceDiagramDragDropEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.dialogs.AddRelatedInstancesDialog;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part.BaseDiagramPartAction;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part.InstanceDiagramEditor;
import org.eclipse.ui.IObjectActionDelegate;

public class AddRelatedInstancesAction extends BaseDiagramPartAction implements
		IObjectActionDelegate {

	public void run(IAction action) {
		IAbstractArtifact[] artifacts = { getCorrespondingArtifact() };
		InstanceMap map = getMap();

		// Set<String> namesOfArtifactsInMap =
		// getNamesOfArtifactsInMap(map.getClassInstances());
		Set<String> namesOfArtifactsInMap = Collections.emptySet();

		ITigerstripeModelProject tsProject = map
				.getCorrespondingITigerstripeProject();
		try {

			Map<String, Set<IAbstractArtifact>> relations = new LinkedHashMap<String, Set<IAbstractArtifact>>();

			IArtifactManagerSession session = tsProject
					.getArtifactManagerSession();
			boolean relationshipsExist = false;

			// Handle extended Artifacts
			Set<IAbstractArtifact> extendedArtifacts = new HashSet<IAbstractArtifact>();
			relations.put("extended", extendedArtifacts);
			for (IAbstractArtifact artifact : artifacts) {
				IAbstractArtifact extendedArtifact = artifact
						.getExtendedArtifact();
				if (extendedArtifact != null
						&& !namesOfArtifactsInMap.contains(extendedArtifact
								.getFullyQualifiedName())) {
					extendedArtifacts.add(extendedArtifact);
					relationshipsExist = true;
				}
			}

			// Handle extending Artifacts
			Set<IAbstractArtifact> extendingArtifacts = new HashSet<IAbstractArtifact>();
			relations.put("extending", extendingArtifacts);
			for (IAbstractArtifact artifact : artifacts) {
				Collection<IAbstractArtifact> extendingArtArray = artifact
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
			relations.put("outgoing", associatedArtifacts);
			for (IAbstractArtifact artifact : artifacts) {
				List<IRelationship> origRels = session
						.getOriginatingRelationshipForFQN(
								artifact.getFullyQualifiedName(), true);
				for (IRelationship relationship : origRels) {
					if (relationship instanceof IAssociationArtifact) {
						IAssociationEnd zEnd = ((IAssociationArtifact) relationship)
								.getZEnd();
						IAbstractArtifact associatedArt = zEnd.getType()
								.getArtifact();

						if (!namesOfArtifactsInMap.contains(associatedArt
								.getFullyQualifiedName())) {
							relationshipsExist = true;
							associatedArtifacts.add(associatedArt);
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
						IAbstractArtifact dependentArt = zEnd.getType()
								.getArtifact();

						if (!namesOfArtifactsInMap.contains(dependentArt
								.getFullyQualifiedName())) {
							relationshipsExist = true;
							dependentArtifacts.add(dependentArt);
						}
					}
				}
			}

			// Handling incoming relationships
			Set<IAbstractArtifact> associatingArtifacts = new HashSet<IAbstractArtifact>();
			Set<IAbstractArtifact> dependingArtifacts = new HashSet<IAbstractArtifact>();
			relations.put("incoming", associatingArtifacts);
			for (IAbstractArtifact artifact : artifacts) {
				List<IRelationship> termRels = session
						.getTerminatingRelationshipForFQN(
								artifact.getFullyQualifiedName(), true);
				for (IRelationship relationship : termRels) {
					if (relationship instanceof IAssociationArtifact) {
						IAssociationEnd aEnd = ((IAssociationArtifact) relationship)
								.getAEnd();
						IAbstractArtifact associatingArt = aEnd.getType()
								.getArtifact();

						if (!namesOfArtifactsInMap.contains(associatingArt
								.getFullyQualifiedName())) {
							relationshipsExist = true;
							associatingArtifacts.add(associatingArt);
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
						IAbstractArtifact dependingArt = aEnd.getType()
								.getArtifact();

						if (!namesOfArtifactsInMap.contains(dependingArt
								.getFullyQualifiedName())) {
							relationshipsExist = true;
							dependingArtifacts.add(dependingArt);
						}
					}
				}
			}

			// Handling implemented Artifacts
			Set<IAbstractArtifact> implementedArtifacts = new HashSet<IAbstractArtifact>();
			relations.put("implemented", implementedArtifacts);
			for (IAbstractArtifact artifact : artifacts) {
				Collection<IAbstractArtifact> implementedArts = artifact
						.getImplementedArtifacts();
				for (IAbstractArtifact implementedArt : implementedArts) {
					if (!namesOfArtifactsInMap.contains(implementedArt
							.getFullyQualifiedName())) {
						relationshipsExist = true;
						implementedArtifacts.add(implementedArt);
					}
				}
			}

			// Handling implementing Artifacts
			Set<IAbstractArtifact> implementingArtifacts = new HashSet<IAbstractArtifact>();
			relations.put("implementing", implementingArtifacts);
			for (IAbstractArtifact artifact : artifacts) {
				Collection<IAbstractArtifact> implementingArts = (artifact)
						.getImplementingArtifacts();
				for (IAbstractArtifact implementingArt : implementingArts) {
					if (!namesOfArtifactsInMap.contains(implementingArt
							.getFullyQualifiedName())) {
						if (!relationshipsExist)
							relationshipsExist = true;
						implementingArtifacts.add(implementingArt);
					}
				}
			}

			// Handling referenced artifacts
			Set<IAbstractArtifact> referencedArtifacts = new HashSet<IAbstractArtifact>();
			relations.put("referenced", referencedArtifacts);
			for (IAbstractArtifact artifact : artifacts) {

				for (IAbstractArtifact referencedArt : artifact
						.getReferencedArtifacts()) {
					if (!namesOfArtifactsInMap.contains(referencedArt
							.getFullyQualifiedName())) {
						relationshipsExist = true;
						referencedArtifacts.add(referencedArt);
					}

				}
			}

			// Handling referencing artifacts
			Set<IAbstractArtifact> referencingArtifacts = new HashSet<IAbstractArtifact>();
			relations.put("referencing", referencingArtifacts);
			for (IAbstractArtifact artifact : artifacts) {
				for (IAbstractArtifact referencingArt : artifact
						.getReferencingArtifacts()) {
					if (!namesOfArtifactsInMap.contains(referencingArt
							.getFullyQualifiedName())) {
						relationshipsExist = true;
						referencingArtifacts.add(referencingArt);
					}
				}
			}

			if (isEmpty(relations)) {
				MessageDialog
						.openInformation(getShell(), "Nothing to add",
								"For the selected items does not exist related artifacts");
				return;
			}

			AddRelatedInstancesDialog dialog = new AddRelatedInstancesDialog(
					getShell(), relations);
			LinkedHashSet<IAbstractArtifact> artifactsToAdd = new LinkedHashSet<IAbstractArtifact>();
			if (dialog.open() == Window.OK) {
				for (Object toAdd : dialog.getResult()) {
					artifactsToAdd.add((IAbstractArtifact) toAdd);
				}
				addToDiagram(
						((InstanceDiagramEditor) getMyTargetWorkbenchPart())
								.getDiagramEditPart(),
						artifactsToAdd);
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}
	}

	protected Set<String> getNamesOfArtifactsInMap(List<?> artifactsInMap) {
		Set<String> namesOfArtifactsInMap = new HashSet<String>(
				artifactsInMap.size());
		for (Object obj : artifactsInMap) {
			if (obj instanceof ClassInstance) {
				namesOfArtifactsInMap.add(((ClassInstance) obj)
						.getFullyQualifiedName());
			}
		}
		return namesOfArtifactsInMap;
	}

	private void addToDiagram(DiagramEditPart mapEditPart,
			Set<IAbstractArtifact> artifactsToAdd) {

		InstanceDiagramDragDropEditPolicy dndEditPolicy = (InstanceDiagramDragDropEditPolicy) mapEditPart
				.getEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE);
		dndEditPolicy.setHost(mapEditPart);
		DropObjectsRequest request = new DropObjectsRequest();
		request.setObjects(new ArrayList<IAbstractArtifact>(artifactsToAdd));
		request.setAllowedDetail(DND.DROP_COPY);
		final Command cmd = dndEditPolicy.getDropObjectsCommand(request);
		if (cmd.canExecute()) {
			cmd.setLabel("Add Related Artifacts");
			ICommand iCommand = new CommandProxy(cmd);
			try {
				OperationHistoryFactory.getOperationHistory().execute(iCommand,
						new NullProgressMonitor(), null);
			} catch (ExecutionException e) {
				EclipsePlugin.log(e);
			}
		}

	}

	private boolean isEmpty(Map<String, Set<IAbstractArtifact>> relations) {
		for (Set<IAbstractArtifact> set : relations.values()) {
			if (!set.isEmpty()) {
				return false;
			}
		}
		return true;
	}
}
