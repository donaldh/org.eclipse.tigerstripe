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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.generator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeGeneratorProject;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeSectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;

public abstract class GeneratorDescriptorSectionPart extends
		TigerstripeSectionPart {

	public GeneratorDescriptorSectionPart(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit, int style) {
		super(page, parent, toolkit, style);
	}

	public ITigerstripeGeneratorProject getIPluggablePluginProject() {
		GeneratorDescriptorEditor editor = (GeneratorDescriptorEditor) getPage()
				.getEditor();
		return editor.getProjectHandle();
	}

	protected boolean projectHasErrors() {
		if (getIPluggablePluginProject().containsErrors()) {
			MessageDialog
					.openError(
							getSection().getShell(),
							"Project contains Errors",
							"This project contains errors. \nPlease fix these errors before deploying it.\nSee ProblemsView for more details.");
			return true;
		}
		return false;
	}

}
