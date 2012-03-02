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
import java.util.Collection;
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
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.AbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.Map;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.adaptation.clazz.dnd.ClassDiagramDragDropEditPolicy;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.edit.parts.MapEditPart;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.wizards.AddRelatedArtifactsWizard;
import org.eclipse.tigerstripe.workbench.ui.visualeditor.diagram.wizards.RelatedCollector;
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
		Set<String> namesOfArtifactsInMap = new HashSet<String>();
		for (Object obj : artifactsInMap) {
			if (obj instanceof AbstractArtifact) {
				namesOfArtifactsInMap.add(((AbstractArtifact) obj)
						.getFullyQualifiedName());
			}
		}
		ITigerstripeModelProject tsProject = map
				.getCorrespondingITigerstripeProject();
		try {
			RelatedCollector collector = new RelatedCollector(namesOfArtifactsInMap, artifacts, tsProject);

			BitSet creationMask = collector.prepare();
			
			if (!creationMask.isEmpty()) {
				
				AddRelatedArtifactsWizard wizard = new AddRelatedArtifactsWizard(creationMask, artifacts, collector);
				WizardDialog dialog = new WizardDialog(shell, wizard);
				dialog.create();
				
				int returnStatus = dialog.open();
				if (returnStatus == Window.OK) {
					Collection<IAbstractArtifact> artifactsToAdd = wizard.getResult();
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
						List artifactsToAddList = new ArrayList();
						for (IAbstractArtifact art : artifactsToAdd) {
							// Bug 310830
							// There is no need for this check - you can simply
							// drop the Artifact
							// IResource artResource = (IResource) art
							// .getAdapter(IResource.class);
							// if (artResource != null)
							// artifactsToAddList.add(artResource
							// .getLocation().toOSString());
							artifactsToAddList.add(art);
						}
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
				if (!collector.isRelationshipsExist()) {
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
}
