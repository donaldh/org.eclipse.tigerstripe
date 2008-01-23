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
package org.eclipse.tigerstripe.workbench.ui.instancediagram.adaptation.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateConnectionViewAndElementRequest.ConnectionViewAndElementDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewAndElementRequest.ViewAndElementDescriptor;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest.ViewDescriptor;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.artifacts.IAssociationArtifact;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeProject;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.AssociationInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.ClassInstance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.Instance;
import org.eclipse.tigerstripe.workbench.ui.instancediagram.InstanceMap;

/**
 * This command is composed with a CreateViewAndElementRequest to populate the
 * newly created EObject based on the corresponding artifact
 * 
 * This is used during a Drag-n-Drop operation
 * 
 * @author Eric Dillon
 * 
 */
public class PostCreationModelUpdateCommand extends
		AbstractTransactionalCommand {

	// private CreateViewAndElementRequest createRequest;

	// private DropObjectsRequest dropRequest;

	private List<IAbstractArtifact> artifacts = new ArrayList<IAbstractArtifact>();

	private List<ViewAndElementDescriptor> descriptors;

	private List<EObject> eObjects = new ArrayList<EObject>();

	private ITigerstripeProject tsProject;

	public PostCreationModelUpdateCommand(TransactionalEditingDomain domain,
			List<ViewAndElementDescriptor> descriptors,
			List<IAbstractArtifact> artifacts, ITigerstripeProject tsProject) {
		super(domain, "PostCreationModelUpdateCommand", null);

		this.descriptors = descriptors;
		this.artifacts = artifacts;
		this.tsProject = tsProject;
	}

	public PostCreationModelUpdateCommand(TransactionalEditingDomain domain,
			Instance eObject, IAbstractArtifact iArtifact,
			ITigerstripeProject tsProject) {
		super(domain, "PostCreationModelUpdateCommand", null);
		this.artifacts.add(iArtifact);
		this.eObjects.add(eObject);
		this.tsProject = tsProject;
	}

	@Override
	public boolean canExecute() {
		// TODO Auto-generated method stub
		boolean canExecute = super.canExecute();
		return canExecute;
	}

	@Override
	protected CommandResult doExecuteWithResult(IProgressMonitor monitor,
			IAdaptable info) throws ExecutionException {

		if (descriptors != null) {
			// this means the constructor based on descriptors was used
			for (ViewDescriptor desc : descriptors) {
				if (desc instanceof ViewAndElementDescriptor) {
					ViewAndElementDescriptor descriptor = (ViewAndElementDescriptor) desc;
					EObject eObj = (EObject) descriptor
							.getCreateElementRequestAdapter().getAdapter(
									EObject.class);
					eObjects.add(eObj);
				} else if (desc instanceof ConnectionViewAndElementDescriptor) {
					ConnectionViewAndElementDescriptor descriptor = (ConnectionViewAndElementDescriptor) desc;
					EObject eObj = (EObject) descriptor
							.getCreateElementRequestAdapter().getAdapter(
									EObject.class);
					eObjects.add(eObj);
				}
			}
		}

		// The update needs to occur in 2 steps.
		// First one pass to set up the names/packages on the newly created
		// objects so we can cross-reference them, and THEN update all the
		// variables/assocs...

		int index = 0;
		for (EObject obj : eObjects) {
			if (obj instanceof ClassInstance) {
				// set name properly

				ClassInstance eArtifact = (ClassInstance) obj;
				IAbstractArtifact iArtifact = artifacts.get(index);
				eArtifact.setName(iArtifact.getName());
				eArtifact.setPackage(iArtifact.getPackage());
			}
		}

		// second pass now.
		index = 0;
		for (EObject obj : eObjects) {
			if (obj instanceof ClassInstance) {
				// set name properly
				ClassInstance eArtifact = (ClassInstance) obj;
				IAbstractArtifact iArtifact = artifacts.get(index);
				InstanceMap map = (InstanceMap) eArtifact.eContainer();

				buildConnections(iArtifact, eArtifact, map, tsProject);
			}
			// if (!(obj instanceof AssociationClass)) // we skip because we
			// know
			// // the following AssociationClassClass is
			// // following and needs the same stuff
			// index++;
		}
		return CommandResult.newOKCommandResult();
	}

	/**
	 * Delegates the post processing of the specifics of each type of artifact
	 * and builds all the necessary connections
	 * 
	 * @param iArtifact
	 * @param eArtifact
	 * @param map
	 */
	private void buildConnections(IAbstractArtifact iArtifact,
			ClassInstance eArtifact, InstanceMap map,
			ITigerstripeProject tsProject) {

		BasePostCreationElementUpdater updater = null;

		// Take care of the outgoing connections first
		if (iArtifact instanceof IAbstractArtifact) {
			// if here, have an AbstractArtifact, need something that will
			// update the contents
			// of the instance (the list of variables)
			updater = new PostCreationClassInstanceUpdater(iArtifact,
					eArtifact, map, tsProject);
		} else if (iArtifact instanceof IAssociationArtifact
				&& eArtifact instanceof AssociationInstance) {
			// if here, then we know that we are on an Association, so need
			// something that
			// will update the contents of the instance (end names,
			// multiplicities, etc.)
			// NOTE: should never end up in here because we don't allow
			// drag-and-drop
			// of association instances, but just in case, here's the code to
			// handle
			// the post-creation update of an association instance...
			updater = new PostCreationAssociationArtifactUpdater(iArtifact,
					(AssociationInstance) eArtifact, map, tsProject);
		}

		if (updater != null) {
			try {
				updater.updateConnections();
			} catch (TigerstripeException e) {
				EclipsePlugin.log(e);
			}
		}
	}

}
