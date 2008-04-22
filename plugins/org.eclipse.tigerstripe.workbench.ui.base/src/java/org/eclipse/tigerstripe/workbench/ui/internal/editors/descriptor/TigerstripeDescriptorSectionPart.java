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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeSectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;

public abstract class TigerstripeDescriptorSectionPart extends
		TigerstripeSectionPart {

	public TigerstripeDescriptorSectionPart(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit, int style) {
		super(page, parent, toolkit, style);
		// TODO Auto-generated constructor stub
	}

	protected ITigerstripeModelProject getTSProject() {
		DescriptorEditor editor = (DescriptorEditor) getPage().getEditor();
		return editor.getTSProject();
	}

	protected boolean isReadonly() {
		return getPage().getEditorInput() instanceof ReadOnlyDescriptorEditorInput;
	}
}
