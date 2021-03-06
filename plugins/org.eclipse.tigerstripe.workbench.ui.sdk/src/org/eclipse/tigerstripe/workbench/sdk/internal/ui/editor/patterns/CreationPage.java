/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.)
 *******************************************************************************/

package org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.patterns;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.workbench.sdk.internal.SDKConstants;
import org.eclipse.tigerstripe.workbench.sdk.internal.ui.editor.naming.NamingSection;
import org.eclipse.tigerstripe.workbench.ui.internal.editors.TigerstripeFormPage;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class CreationPage extends TigerstripeFormPage {

	private IManagedForm managedForm;
	private PatternSection patternSection;
	private DisabledPatternSection disabledPatternSection;
	private NamingSection namingSection;

	public static final String PAGE_ID = "org.eclipse.tigerstripe.sdk.extension.patterns"; 

	public CreationPage(FormEditor editor) {
		super(editor, PAGE_ID, "Creation");
	}

	public CreationPage() {
		super(PAGE_ID, "Creation");
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		this.managedForm = managedForm;
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();

		form.setText("Contributions to '"+SDKConstants.PATTERNS_EXT_PT+"'");
		fillBody(managedForm, toolkit);
		managedForm.refresh();
	}

	@Override
	public void refresh() {
		if (managedForm != null) {
			managedForm.refresh();
		}
		if (patternSection != null){
			patternSection.refresh();
		}
		if (disabledPatternSection != null){
			disabledPatternSection.refresh();
		}
		if (namingSection != null){
			namingSection.refresh();
		}
	}

	private void fillBody(IManagedForm managedForm, FormToolkit toolkit) {
		Composite body = managedForm.getForm().getBody();
		TableWrapLayout layout = new TableWrapLayout();
		layout.bottomMargin = 10;
		layout.topMargin = 5;
		layout.leftMargin = 10;
		layout.rightMargin = 10;
		layout.numColumns = 2;
		layout.verticalSpacing = 30;
		layout.horizontalSpacing = 10;
		body.setLayout(layout);

		patternSection = new PatternSection(this,
				body, toolkit, ExpandableComposite.EXPANDED);
		managedForm.addPart(patternSection);
		disabledPatternSection = new DisabledPatternSection(this,
				body, toolkit, ExpandableComposite.EXPANDED);
		managedForm.addPart(disabledPatternSection);
		namingSection = new NamingSection(this,
				body, toolkit, ExpandableComposite.EXPANDED);
		managedForm.addPart(namingSection);
		
	}

}
