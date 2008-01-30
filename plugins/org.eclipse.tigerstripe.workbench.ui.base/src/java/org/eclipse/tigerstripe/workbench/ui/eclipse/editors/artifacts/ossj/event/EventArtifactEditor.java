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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.event;

import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IOssjLegacySettigsProperty;
import org.eclipse.tigerstripe.workbench.internal.api.profile.properties.IWorkbenchPropertyLabels;
import org.eclipse.tigerstripe.workbench.internal.core.profile.properties.OssjLegacySettingsProperty;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.OssjArtifactOverviewPage;
import org.eclipse.ui.PartInitException;

/**
 * An Oss/j Entity Editor
 * 
 * @author Eric Dillon
 * 
 */
public class EventArtifactEditor extends ArtifactEditorBase {

	private int specificsPage;

	private OssjEventSpecificsPage specPage;

	public EventArtifactEditor() {
		super();
	}

	@Override
	protected void addPages() {
		super.addPages();
		int index = -1;
		try {
			OssjArtifactOverviewPage page = new OssjArtifactOverviewPage(this,
					new EventArtifactLabelProvider(),
					new EventArtifactFormContentProvider());
			index = addPage(page);
			addModelPage(page);

			OssjLegacySettingsProperty prop = (OssjLegacySettingsProperty) TigerstripeCore
					.getWorkbenchProfileSession().getActiveProfile()
					.getProperty(IWorkbenchPropertyLabels.OSSJ_LEGACY_SETTINGS);

			if (prop
					.getPropertyValue(IOssjLegacySettigsProperty.DISPLAY_OSSJSPECIFICS)) {
				specPage = new OssjEventSpecificsPage(this,
						new EventArtifactLabelProvider(),
						new EventArtifactFormContentProvider());
				specificsPage = addPage(specPage);
				addModelPage(specPage);
			}
		} catch (PartInitException e) {
			EclipsePlugin.log(e);
		}
		setActivePage(index);
	}

}
