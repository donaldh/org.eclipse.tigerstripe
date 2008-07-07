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
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jdt.ui.actions.RenameAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelChangeRequestFactory;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.IModelUpdater;
import org.eclipse.tigerstripe.workbench.internal.api.model.artifacts.updater.request.IArtifactRenameRequest;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IArtifactManagerSession;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IPackageArtifact;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TSExplorerUtils;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.AbstractLogicalExplorerNode;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.abstraction.action.LogicalNodeRenameAction;
import org.eclipse.ui.IWorkbenchSite;

public class TSRenameAction extends RenameAction {

	public TSRenameAction(IWorkbenchSite site) {
		super(site);
	}

	public TSRenameAction(CompilationUnitEditor editor) {
		super(editor);
	}

	@Override
	public void run(IStructuredSelection selection) {
		if (selection.getFirstElement() instanceof AbstractLogicalExplorerNode) {
			LogicalNodeRenameAction action = new LogicalNodeRenameAction(
					"Rename", getShell());
			action.selectionChanged(selection);
			action.run();
		} else if (selection.getFirstElement() instanceof IPackageFragment){
			
			// grab the packageArtifact - if there is one! there should always be if
			// it is enabled
			
			
			IPackageArtifact packageArtifact = null;
			IJavaElement jElem = (IJavaElement) selection.getFirstElement();
			IAbstractArtifact artifact = null;
			artifact = TSExplorerUtils.getArtifactFor(jElem);
			if (artifact != null && artifact instanceof IPackageArtifact){
				packageArtifact = (IPackageArtifact) artifact;
				
			}


			// rename the real package
			super.run(selection);
			// Then (if necessary) do the .package contents
			// but what do we rename it to ?

			if (packageArtifact != null){
				try {
					IResource res = (IResource) packageArtifact.getAdapter(IResource.class);
					// The resource is the *MOVED* resource so we can derive the new FQN from that
					IResource parentPackage = res.getParent();
					IJavaElement myPackageFragment = JavaCore.create(parentPackage);
					String fqn = myPackageFragment.getElementName();
					
					// Make a rename request
					IArtifactManagerSession mgr = packageArtifact.getProject().getArtifactManagerSession();
					IModelUpdater updater = mgr.getIModelUpdater();
					
					IArtifactRenameRequest request = (IArtifactRenameRequest) updater
					.getRequestFactory().makeRequest(
							IModelChangeRequestFactory.ARTIFACT_RENAME);
					request.setArtifactFQN(packageArtifact.getFullyQualifiedName());
					
					// TODO FIX THIS FOR THE FULL FQN VERSION
					String name = fqn.substring(fqn.lastIndexOf(".")+1);
					request.setNewName(name);
					
					updater.handleChangeRequest(request);
					
					
				} catch (Exception e){
					
					EclipsePlugin.log(e);
				}

			}



		} else
			super.run(selection);

	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		super.selectionChanged(event);
		if (event.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) event
					.getSelection();
			if (ssel.getFirstElement() instanceof AbstractLogicalExplorerNode) {
				setEnabled(((AbstractLogicalExplorerNode) ssel
						.getFirstElement()).canBeRenamed());
			}
		}
	}

}
