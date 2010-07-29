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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.association;

import org.eclipse.tigerstripe.workbench.ui.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactEditorBase;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.ArtifactOverviewPage;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;
import org.eclipse.ui.PartInitException;

/**
 * An Oss/j Datatype Editor
 * 
 * @author Eric Dillon
 * 
 */
public class AssociationArtifactEditor extends ArtifactEditorBase {

	public AssociationArtifactEditor() {
		super();
		setTitleImage(Images.get(Images.ASSOCIATION_ICON));
	}

	@Override
	protected void addPages() {
		int index = -1;
		try {
			ArtifactOverviewPage page = new ArtifactOverviewPage(this,
					new AssociationArtifactLabelProvider(),
					new AssociationArtifactFormContentProvider());
			index = addPage(page);
			addModelPage(page);

			// OssjDatatypeSpecificsPage specPage = new
			// OssjDatatypeSpecificsPage(
			// this, new AssociationArtifactLabelProvider(),
			// new AssociationArtifactFormContentProvider());
			// addPage(specPage);
			// addModelPage(specPage);
		} catch (PartInitException e) {
			EclipsePlugin.log(e);
		}
		super.addPages();
		setActivePage(index);
	}
}
