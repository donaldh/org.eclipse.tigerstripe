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

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.tigerstripe.workbench.ui.internal.utils.ColorUtils;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.Hyperlink;

public abstract class TigerstripeFormPage extends FormPage {

	private Control lastFocusControl;
	private boolean neverShow = true;

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

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);

		IManagedForm managedForm = getManagedForm();
		if (managedForm != null) {
			addLastFocusListeners(managedForm.getForm());
		}
	}

	public void addLastFocusListeners(Composite composite) {
		Control[] controls = composite.getChildren();
		for (int i = 0; i < controls.length; i++) {
			Control control = controls[i];
			if ((control instanceof Text) || (control instanceof Button)
					|| (control instanceof Combo)
					|| (control instanceof CCombo) || (control instanceof Tree)
					|| (control instanceof Table)
					|| (control instanceof Spinner)
					|| (control instanceof Link) || (control instanceof List)
					|| (control instanceof TabFolder)
					|| (control instanceof CTabFolder)
					|| (control instanceof Hyperlink)
					|| (control instanceof FilteredTree)) {

				addLastFocusListener(control);
			}
			if (control instanceof Composite) {
				// Recursively add focus listeners to this composites children
				addLastFocusListeners((Composite) control);
			}
		}
	}

	private void addLastFocusListener(final Control control) {
		control.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				// NO-OP
			}

			public void focusLost(FocusEvent e) {
				lastFocusControl = control;
			}
		});
	}

	public void updateFormSelection() {
		if (neverShow) {
			neverShow = false;
			Control control = getFocusedControl();
			if (control != null && !control.isDisposed()) {
				control.setFocus();
			}
		} else {
			if ((lastFocusControl != null) && !lastFocusControl.isDisposed()) {
				Control lastControl = lastFocusControl;
				lastControl.forceFocus();
				if (lastControl instanceof Text) {
					Text text = (Text) lastControl;
					text.setSelection(0, text.getText().length());
				}
			} else {
				setFocus();
			}
		}
	}

	public abstract void refresh();
}
