/******************************************************************************* 
 * Copyright (c) 2011 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Anton Salnik) 
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.editors.descriptor.facetRefs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeSectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class FacetReferencesDescriptionSection extends TigerstripeSectionPart {

	public FacetReferencesDescriptionSection(TigerstripeFormPage page,
			Composite parent, FormToolkit toolkit) {
		super(page, parent, toolkit, ExpandableComposite.TITLE_BAR);
		setTitle("Details");
		createContent();
	}

	@Override
	protected void createContent() {
		GridData td = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		td.horizontalSpan = 2;
		td.widthHint = 200;
		getSection().setLayoutData(td);

		createDescription(getBody(), getToolkit());

		getSection().setClient(getBody());
		getToolkit().paintBordersFor(getBody());
	}

	@Override
	public void setFocus() {
		super.setFocus();
	}

	private void createDescription(Composite parent, FormToolkit toolkit) {
		FormText rtext = toolkit.createFormText(parent, false);
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB,
				TableWrapData.TOP);
		td.colspan = 2;
		rtext.setLayoutData(td);
		rtext.setText(
				"<form><p>The Facets specified on this page are used at generation"
						+ " time only. To activate a Facet such that exclusions are displayed"
						+ " in Tigerstripe Explorer and Diagrams, right click on the"
						+ " Facet > Select 'Facets' > 'Mark As Active'. Note that there can"
						+ " only be one active Facet at a given time.</p></form>",
				true, false);
	}
}
