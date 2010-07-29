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
package org.eclipse.tigerstripe.workbench.ui.internal.editors.segment.header;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeSectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class WelcomeSection extends TigerstripeSectionPart {

	public WelcomeSection(TigerstripeFormPage page, Composite parent,
			FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.NO_TITLE);
		createContent();
	}

	@Override
	protected void createContent() {
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		getSection().setLayoutData(td);

		createWelcomeMsg(getBody(), getToolkit());

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	private void createWelcomeMsg(Composite parent, FormToolkit toolkit) {
		TableWrapData td = null;
		String data = "<form><p><b>Tigerstripe Workbench Facet Editor.</b></p></form>";
		FormText rtext = toolkit.createFormText(parent, true);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		rtext.setLayoutData(td);
		rtext.setText(data, true, false);
	}
}
