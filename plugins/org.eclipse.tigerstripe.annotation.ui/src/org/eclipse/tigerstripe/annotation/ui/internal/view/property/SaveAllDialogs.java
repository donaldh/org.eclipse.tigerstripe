/******************************************************************************* 
 * Copyright (c) 2008 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.ui.internal.view.property;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;

/**
 * @author Yuri Strot
 *
 */
public class SaveAllDialogs extends Dialog {
	
	private Annotation[] annotations;
	private Table table;

	/**
	 * @param parentShell
	 */
	protected SaveAllDialogs(Shell parentShell, Annotation[] annotations) {
		super(parentShell);
	    setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
	    this.annotations = annotations;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Save Dialog");
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite)super.createDialogArea(parent);
		composite.setLayout(new GridLayout(2, false));
		
		Label label = new Label(composite, SWT.LEFT | SWT.WRAP);
		label.setText("Following annotations has been modified. Select which you want to save.");
		GridData data = new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1);
		data.widthHint = 250;
		label.setLayoutData(data);
		
		table = new Table(composite, SWT.CHECK | SWT.BORDER);
		initTable();
		table.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite buttons = new Composite(composite, SWT.NONE);
		buttons.setLayoutData(new GridData(GridData.FILL_BOTH));
		initButtons(buttons);
		
	    return composite;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
	}
	
	private void initTable() {
		table.setHeaderVisible(false);
		table.setLinesVisible(false);
		for (int i = 0; i < annotations.length; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(getDisplayName(annotations[i]));
			item.setData(annotations[i]);
		}
	}
	
	private void initButtons(Composite composite) {
		composite.setLayout(new GridLayout());
		createButton(composite, "Select All", new Runnable() {
			public void run() {
				for (TableItem item : table.getItems()) {
					item.setChecked(true);
				}
			}
		}, false);
		createButton(composite, "Deselect All", new Runnable() {
			public void run() {
				for (TableItem item : table.getItems()) {
					item.setChecked(false);
				}
			}
		}, false);
		createButton(composite, "OK", new Runnable() {
			public void run() {
				for (TableItem item : table.getItems()) {
					if (item.getChecked()) {
						Annotation annotation = (Annotation)item.getData();
						AnnotationPlugin.getManager().save(annotation);
					}
				}
				okPressed();
			}
		}, true);
	}
	
	protected Button createButton(Composite parent, String label,
			final Runnable runnable, boolean defaultButton) {
		// increment the number of columns in the button bar
		Button button = new Button(parent, SWT.PUSH);
		button.setText(label);
		button.setFont(JFaceResources.getDialogFont());
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				runnable.run();
			}
		});
		if (defaultButton) {
			Shell shell = parent.getShell();
			if (shell != null) {
				shell.setDefaultButton(button);
			}
		}
		setButtonLayoutData(button);
		return button;
	}
	
	protected String getDisplayName(Annotation annotation) {
		EObject content = annotation.getContent();
		if (content == null) {
			return "<no content>";
		}
		else {
			AnnotationType type = AnnotationPlugin.getManager().getType(annotation);
			if (type != null) {
				ILabelProvider provider = AnnotationUIPlugin.getManager().getLabelProvider(type);
				if (provider != null)
					return provider.getText(content);
			}
			return content.eClass().getName() + "@" + Integer.toHexString(content.hashCode());
		}
	}

}
