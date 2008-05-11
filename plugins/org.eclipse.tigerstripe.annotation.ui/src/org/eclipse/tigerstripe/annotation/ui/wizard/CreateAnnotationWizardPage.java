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
package org.eclipse.tigerstripe.annotation.ui.wizard;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;


/**
 * @author Yuri Strot
 *
 */
public class CreateAnnotationWizardPage extends WizardPage {
	
	private static final String TITLE = "Select Annotation Type";
	private String uri;
	private AnnotationType type;
	private AnnotationType[] types;
	private Table combo;
	
	private ComboListener comboListener;

	public CreateAnnotationWizardPage() {
	    super(TITLE);
	    setTitle(TITLE);
    }
	
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public AnnotationType getType() {
		return type;
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		
		Composite composite= new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;		
		composite.setLayout(layout);
		
		createTypeControls(composite);
		
		setControl(composite);
		Dialog.applyDialogFont(composite);
    }
	
	protected void createURIControls(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText("URI: ");
		
		Text content = new Text(parent, SWT.READ_ONLY);
		content.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		content.setText(uri);
		content.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}
	
	protected void createTypeControls(Composite parent) {
		Label label = new Label(parent, SWT.TOP);
		label.setText("Type: ");
		label.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		types = AnnotationPlugin.getManager().getTypes();
		
		combo = new Table(parent, SWT.BORDER);
		combo.setHeaderVisible(false);
		combo.setLinesVisible(false);
		for (int i = 0; i < types.length; i++) {
			TableItem item = new TableItem(combo, SWT.NONE);
			item.setText(types[i].getName());
			ImageDescriptor descriptor = AnnotationUIPlugin.getManager(
				).getImage(types[i]);
			if (descriptor != null)
				item.setImage(descriptor.createImage());
		}
		combo.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		comboListener = new ComboListener();
		combo.addSelectionListener(comboListener);
		combo.select(0);
		combo.forceFocus();
		comboListener.updateDescription();
	}
	
	private class ComboListener implements SelectionListener  {
		
		public void widgetSelected(SelectionEvent e) {
			updateDescription();
		}
	
		public void widgetDefaultSelected(SelectionEvent e) {
			updateDescription();
		}
		
		public void updateDescription() {
			type = types[combo.getSelectionIndex()];
			String d = type.getDesciption();
			if (d == null) d = "";
			setMessage(d);
		}
	
	}

}
