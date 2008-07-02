/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - rcraddoc
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.actions;

import java.util.Iterator;

import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.ui.actions.OpenAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.TigerstripeLog;
import org.eclipse.tigerstripe.workbench.ui.internal.views.explorerview.TSExplorerUtils;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.PlatformUI;

public class OpenPackageArtifactEditorAction extends OpenAction {

	public OpenPackageArtifactEditorAction(IWorkbenchSite site) {
		super(site);

	}

	// If we have got here, then we should
	// Check that a .package file exists in this package.
	// and if it does NOT:
	// create one
	// Then
	// open the Package editor on that artifact

	@Override
	public void run(IStructuredSelection selection) {
		if (!checkEnabled(selection))
			return;
		run(selection.toArray());
	}

	private boolean checkEnabled(IStructuredSelection selection) {
		if (selection.isEmpty())
			return false;
		for (Iterator<?> iter = selection.iterator(); iter.hasNext();) {
			Object element = iter.next();

			if (element instanceof IPackageFragment)
				continue;
		}
		return true;
	}

	@Override
	public void run(Object[] objects) {
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		if (page == null)
			return;
		for (int i = 0; i < objects.length; i++) {
			IPackageFragment fragment = (IPackageFragment) objects[i];
			// Should have a ".package" as the package Artifact....
			// try to get the artifact form the manager
			try {
				IAbstractArtifact artifact = TSExplorerUtils
						.getArtifactFor(fragment);
				if (artifact != null) {
					TSOpenAction.openEditor(artifact, page);
					// // This is noi longer used as we always create a "dummy"
					// packge artifact
					// } else {
					// // The artifact may be null if it was not created using
					// the wizards etc.
					// // So create one and add it to the artifact manager.
					// IAbstractTigerstripeProject aProject = TigerstripeCore
					// .findProject(fragment.getCorrespondingResource().getProject().getLocation()
					// .toFile().toURI());
					//
					// if (aProject instanceof ITigerstripeModelProject) {
					// ITigerstripeModelProject project =
					// (ITigerstripeModelProject) aProject;
					// IArtifactManagerSession mgr = project
					// .getArtifactManagerSession();
					//
					// String packageName = fragment.getElementName();
					// artifact = PackageArtifact.makeArtifactForPackage(mgr,
					// packageName);
					// if (artifact != null){
					// TSOpenAction.openEditor(artifact, page);
					// }
					//						
					//						
					// }

				}

			} catch (Exception e) {
				TigerstripeLog.logError(
						"Could not extract or create package artifact", e);
			}
		}

	}

}
