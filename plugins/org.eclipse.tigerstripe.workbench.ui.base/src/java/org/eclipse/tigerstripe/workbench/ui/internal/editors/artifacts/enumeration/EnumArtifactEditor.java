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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.artifacts.enumeration;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IEnumArtifact;
import org.eclipse.tigerstripe.workbench.model.deprecated_.ILiteral;
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
public class EnumArtifactEditor extends ArtifactEditorBase {

	public EnumArtifactEditor() {
		super();
		setTitleImage(Images.get(Images.ENUM_ICON));
	}

	@Override
	protected void addPages() {
		int index = -1;
		try {
			ArtifactOverviewPage page = createOverviewPage();
			index = addPage(page);
			addModelPage(page);

			OssjEnumSpecificsPage specPage = new OssjEnumSpecificsPage(this,
					new EnumArtifactLabelProvider(),
					new EnumArtifactFormContentProvider());
			addPage(specPage);
			addModelPage(specPage);
		} catch (PartInitException e) {
			EclipsePlugin.log(e);
		}
		super.addPages();
		setActivePage(index);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		boolean literalsChanged = false;
		try {
			IEnumArtifact wsEnumArtifact = (IEnumArtifact) getIArtifact();
			IEnumArtifact enumArtifact = (IEnumArtifact) wsEnumArtifact
					.getProject()
					.getArtifactManagerSession()
					.getArtifactByFullyQualifiedName(
							wsEnumArtifact.getFullyQualifiedName(), false);
			for (ILiteral literal : enumArtifact.getLiterals()) {
				boolean found = false;
				for (ILiteral wsLiteral : wsEnumArtifact.getLiterals()) {
					if (literal.getName().equals(wsLiteral.getName())) {
						found = true;
						break;
					}
				}
				if (!found) {
					literalsChanged = true;
					break;
				}
			}
		} catch (TigerstripeException e) {
			EclipsePlugin.log(e);
		}

		if (literalsChanged
				&& !MessageDialog
						.openQuestion(
								getSite().getShell(),
								"Save Enumeration",
								"Enumeration literals were changed, this may affect all artifacts referencing the Enumeration. Do you wish to proceed?")) {
			return;
		}

		super.doSave(monitor);
	}
}
