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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.packageArtifact;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactOverviewPage;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.ui.PartInitException;

/**
 * An Oss/j Entity Editor
 * 
 * @author Eric Dillon
 * 
 */
public class PackageArtifactEditor extends ArtifactEditorBase {

	public PackageArtifactEditor() {
		super();
		setTitleImage(Images.get(Images.PACKAGE_ICON));
	}

	@Override
	protected void addPages() {
		int index = -1;
		try {
			ArtifactOverviewPage page = createOverviewPage();
			index = addPage(page);
			addModelPage(page);
			OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
					.getWorkbenchProfileSession().getActiveProfile()
					.getProperty(IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);

			if (prop
					.getPropertyValue(IOssjLegacySettigsProperty.DISPLAY_OSSJSPECIFICS)) {
				PackageSpecificsPage specPage = new PackageSpecificsPage(this,
						new PackageArtifactLabelProvider(),
						new PackageArtifactFormContentProvider());
				addPage(specPage);
				addModelPage(specPage);
			}
		} catch (PartInitException e) {
			EclipsePlugin.log(e);
		}
		super.addPages();
		setActivePage(index);
	}

	// Override this to provide information when starting a wizard

	public Object getViewPartInput() {
		// The input does not adapt to IJavaElement because it's a .package,
		// So we need to trick it!
		IResource res = (IResource) getEditorInput()
				.getAdapter(IResource.class);
		if (res != null) {
			return res.getParent().getAdapter(IJavaElement.class);
		}
		return null;
	}

}
