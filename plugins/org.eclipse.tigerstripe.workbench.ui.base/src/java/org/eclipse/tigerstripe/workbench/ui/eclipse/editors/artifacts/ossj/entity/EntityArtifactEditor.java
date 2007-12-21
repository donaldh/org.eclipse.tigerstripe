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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.entity;

import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.OssjArtifactOverviewPage;
import org.eclipse.ui.PartInitException;

/**
 * An Oss/j Entity Editor
 * 
 * @author Eric Dillon
 * 
 */
public class EntityArtifactEditor extends ArtifactEditorBase {

	private OssjEntitySpecificsPage specPage;

	public EntityArtifactEditor() {
		super();
	}

	@Override
	protected void addPages() {
		super.addPages();
		int index = -1;
		try {
			OssjArtifactOverviewPage page = new OssjArtifactOverviewPage(this,
					new EntityArtifactLabelProvider(),
					new EntityArtifactFormContentProvider());
			index = addPage(page);
			addModelPage(page);

			specPage = new OssjEntitySpecificsPage(this,
					new EntityArtifactLabelProvider(),
					new EntityArtifactFormContentProvider());
			addPage(specPage);
			addModelPage(specPage);
		} catch (PartInitException e) {
			EclipsePlugin.log(e);
		}
		setActivePage(index);
	}
}
