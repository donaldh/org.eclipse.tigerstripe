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
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.CommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.EditPolicyRoles;
import org.eclipse.gmf.runtime.diagram.ui.requests.DropObjectsRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.dnd.DND;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAssociationEnd;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IDependencyArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IRelationship.IRelationshipEnd;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.dnd.InstanceDiagramDragDropEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.dialogs.AddRelatedInstancesDialog;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.edit.policies.ClassInstanceItemSemanticEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part.BaseDiagramPartAction;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.part.InstanceDiagramEditor;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.diagram.providers.InstanceElementTypes;
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

			// handle outgoing relationships
			Set<IAbstractArtifact> associatedArtifacts = new HashSet<IAbstractArtifact>();
			Set<IAbstractArtifact> dependentArtifacts = new HashSet<IAbstractArtifact>();
			relations.put("outgoing associated", associatedArtifacts);
			relations.put("outgoing dependent", dependentArtifacts);
			for (IAbstractArtifact artifact : artifacts) {

				Set<String> hierarhy = new HashSet<String>();
				featchHierarhyUp(artifact, hierarhy);

				Set<IRelationship> seen = new HashSet<IRelationship>();
				for (String fqn : hierarhy) {
					Collection<IRelationship> origRels = new HashSet<IRelationship>(
							session.getOriginatingRelationshipForFQN(fqn, true));
					origRels.removeAll(seen);
					seen.addAll(origRels);

					for (IRelationship relationship : origRels) {
						if (relationship instanceof IAssociationArtifact) {
							IAssociationEnd zEnd = ((IAssociationArtifact) relationship)
									.getZEnd();

							Set<IAbstractArtifact> downHierarhy = new HashSet<IAbstractArtifact>();
							featchHierarhyDown(zEnd.getType().getArtifact(),
									downHierarhy);
							for (IAbstractArtifact associatedArt : downHierarhy) {
								if (!namesOfArtifactsInMap
										.contains(associatedArt
												.getFullyQualifiedName())) {
									associatedArtifacts.add(associatedArt);
								}
							}
						} else if (relationship instanceof IDependencyArtifact) {
							IRelationshipEnd zEnd = ((IDependencyArtifact) relationship)
									.getRelationshipZEnd();

							Set<IAbstractArtifact> downHierarhy = new HashSet<IAbstractArtifact>();
							featchHierarhyDown(zEnd.getType().getArtifact(),
									downHierarhy);
							for (IAbstractArtifact dependentArt : downHierarhy) {
								if (!namesOfArtifactsInMap
										.contains(dependentArt
												.getFullyQualifiedName())) {
									dependentArtifacts.add(dependentArt);
								}
							}
						}
					}
				}

			}

			// Handling incoming relationships
			Set<IAbstractArtifact> associatingArtifacts = new HashSet<IAbstractArtifact>();
			Set<IAbstractArtifact> dependingArtifacts = new HashSet<IAbstractArtifact>();
			relations.put("incoming associating", associatingArtifacts);
			relations.put("incoming depending", dependingArtifacts);
			for (IAbstractArtifact artifact : artifacts) {

				Set<String> hierarhy = new HashSet<String>();
				featchHierarhyUp(artifact, hierarhy);

				Set<IRelationship> seen = new HashSet<IRelationship>();
				for (String fqn : hierarhy) {
					Collection<IRelationship> termRels = new HashSet<IRelationship>(
							session.getTerminatingRelationshipForFQN(fqn, true));
					termRels.removeAll(seen);
					seen.addAll(termRels);

					for (IRelationship relationship : termRels) {
						if (relationship instanceof IAssociationArtifact) {
							IAssociationEnd aEnd = ((IAssociationArtifact) relationship)
									.getAEnd();

							Set<IAbstractArtifact> downHierarhy = new HashSet<IAbstractArtifact>();
							featchHierarhyDown(aEnd.getType().getArtifact(),
									downHierarhy);
							for (IAbstractArtifact associatingArt : downHierarhy) {
								if (!namesOfArtifactsInMap
										.contains(associatingArt
												.getFullyQualifiedName())) {
									associatingArtifacts.add(associatingArt);
								}
							}
						} else if (relationship instanceof IDependencyArtifact) {
							IRelationshipEnd aEnd = ((IDependencyArtifact) relationship)
									.getRelationshipAEnd();

							Set<IAbstractArtifact> downHierarhy = new HashSet<IAbstractArtifact>();
							featchHierarhyDown(aEnd.getType().getArtifact(),
									downHierarhy);
							for (IAbstractArtifact dependingArt : downHierarhy) {
								if (!namesOfArtifactsInMap
										.contains(dependingArt
												.getFullyQualifiedName())) {
									dependingArtifacts.add(dependingArt);
								}
							}
						}
					}
				}
			}

			if (isEmpty(relations)) {
				MessageDialog.openInformation(getShell(), "Nothing to add",
						"Selected item does not have any related artifacts");
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

	private void featchHierarhyUp(IAbstractArtifact artifact, Set<String> set) {
		if (artifact == null) {
			return;
		}
		if (!set.add(artifact.getFullyQualifiedName())) {
			return;
		}
		featchHierarhyUp(artifact.getExtendedArtifact(), set);
		for (IAbstractArtifact impl : artifact.getImplementingArtifacts()) {
			featchHierarhyUp(impl, set);
		}
	}

	private void featchHierarhyDown(IAbstractArtifact artifact,
			Set<IAbstractArtifact> set) {
		if (artifact == null) {
			return;
		}
		if (!set.add(artifact)) {
			return;
		}
		for (IAbstractArtifact extending : artifact.getExtendingArtifacts()) {
			featchHierarhyDown(extending, set);
		}
		for (IAbstractArtifact impl : artifact.getImplementingArtifacts()) {
			featchHierarhyDown(impl, set);
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

		Set<ClassInstance> oldState = new HashSet<ClassInstance>(getMap()
				.getClassInstances());

		InstanceDiagramDragDropEditPolicy dndEditPolicy = (InstanceDiagramDragDropEditPolicy) mapEditPart
				.getEditPolicy(EditPolicyRoles.DRAG_DROP_ROLE);
		dndEditPolicy.setHost(mapEditPart);
		DropObjectsRequest request = new DropObjectsRequest();
		request.setObjects(new ArrayList<IModelComponent>(artifactsToAdd));
		request.setAllowedDetail(DND.DROP_COPY);

		exec(dndEditPolicy.getDropObjectsCommand(request));

		Set<ClassInstance> newObjects = new HashSet<ClassInstance>(getMap()
				.getClassInstances());
		newObjects.removeAll(oldState);

		CompoundCommand addRelationsCommand = new CompoundCommand();
		ClassInstanceItemSemanticEditPolicy ciEditPolicy = new ClassInstanceItemSemanticEditPolicy();
		ciEditPolicy.setHost(mySelectedElement);

		for (ClassInstance ci : newObjects) {
			CreateRelationshipRequest req = new CreateRelationshipRequest(
					InstanceElementTypes.AssociationInstance_3001);
			req.setSource(getCorrespondingEObject());
			req.setTarget(ci);
			addRelationsCommand.add(ciEditPolicy
					.getCreateRelationshipCommand(req));

			req = new CreateRelationshipRequest(
					InstanceElementTypes.AssociationInstance_3001);
			req.setSource(ci);
			req.setTarget(getCorrespondingEObject());
			addRelationsCommand.add(ciEditPolicy
					.getCreateRelationshipCommand(req));

			// link with each other
			for (ClassInstance ci2 : newObjects) {
				if (ci2.equals(ci)) {
					continue;
				}

				CreateRelationshipRequest req2 = new CreateRelationshipRequest(
						InstanceElementTypes.AssociationInstance_3001);
				req2.setSource(ci);
				req2.setTarget(ci2);
				addRelationsCommand.add(ciEditPolicy
						.getCreateRelationshipCommand(req2));
			}
		}

		exec(addRelationsCommand);
	}

	private void exec(Command cmd) {
		if (cmd.canExecute()) {
			cmd.setLabel("Add Related Artifacts");
			CommandProxy command = new CommandProxy(cmd);
			try {
				OperationHistoryFactory.getOperationHistory().execute(command,
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
