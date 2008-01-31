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
package org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.dependency;

import org.eclipse.tigerstripe.workbench.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.eclipse.editors.artifacts.ossj.OssjArtifactOverviewPage;
import org.eclipse.ui.PartInitException;

/**
 * A UML Dependency Editor
 * 
 * @author Eric Dillon
 * @since 1.2
 */
public class DependencyArtifactEditor extends ArtifactEditorBase {

	public DependencyArtifactEditor() {
		super();
	}

	@Override
	protected void addPages() {
		super.addPages();
		int index = -1;
		try {
			OssjArtifactOverviewPage page = new OssjArtifactOverviewPage(this,
					new DependencyArtifactLabelProvider(),
					new DependencyArtifactFormContentProvider());
			index = addPage(page);
			addModelPage(page);

		} catch (PartInitException e) {
			EclipsePlugin.log(e);
		}
		setActivePage(index);
	}
}
