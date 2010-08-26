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
package org.eclipse.tigerstripe.workbench.ui.internal.editors;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.ColorUtils;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.Form;

public abstract class TigerstripeFormPage extends FormPage implements IFocusedControlProvider {

	public TigerstripeFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	public TigerstripeFormPage(String id, String title) {
		super(id, title);
	}
	
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		Form form = managedForm.getForm().getForm();
		setTSLooknfeel(managedForm, form);
	}
	
	public Control getFocusedControl() {
		for (IFormPart part : getManagedForm().getParts()) {
			if (part instanceof IFocusedControlProvider) {
				return ((IFocusedControlProvider) part).getFocusedControl();
			}
		}
		return findFirstTextComponent(getPartControl());
	}

	private Control findFirstTextComponent(Control control) {
		if (!(control.getVisible() && control.getEnabled())) {
			return null;
		}
		if (control instanceof Composite) {
			Composite composite = (Composite) control; 
			for (Control child : composite.getChildren()) {
				Control textControl = findFirstTextComponent(child);
				if (textControl != null) {
					return textControl; 
				}
			}
			return null;
		} else if (control instanceof Text) {
			return control;
		} else {
			return null;
		}
	}

	protected void setTSLooknfeel(IManagedForm managedForm, Form form) {
		managedForm.getToolkit().decorateFormHeading(form);
		form.setHeadColor(IFormColors.H_BOTTOM_KEYLINE2, ColorUtils.TS_ORANGE);
	}

	public abstract void refresh();
}
