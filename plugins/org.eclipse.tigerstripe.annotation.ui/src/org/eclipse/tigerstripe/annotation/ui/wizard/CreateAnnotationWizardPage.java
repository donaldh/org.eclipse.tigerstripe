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
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.core.AnnotationType;
import org.eclipse.tigerstripe.annotation.core.TargetAnnotationType;
import org.eclipse.tigerstripe.annotation.ui.AnnotationUIPlugin;


/**
 * @author Yuri Strot
 *
 */
public class CreateAnnotationWizardPage extends WizardPage {
	
	private static final String TITLE = "Select Annotation Type";
	private TargetAnnotationType type;
	private TargetAnnotationType[] types;
	private Table combo;
	private Object object;
	
	private ComboListener comboListener;

	public CreateAnnotationWizardPage(Object object) {
	    super(TITLE);
	    setTitle(TITLE);
	    this.object = object;
    }
	
	public TargetAnnotationType getType() {
		return type;
	}

	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		setMessage("No possible types");
		
		Composite composite= new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;		
		composite.setLayout(layout);
		
		createTypeControls(composite);
		
		setControl(composite);
		Dialog.applyDialogFont(composite);
    }
	
	public boolean canFinish() {
		return combo != null && !combo.isDisposed() && combo.getItemCount() > 0;
	}
	
	protected void createTypeControls(Composite parent) {
		Label label = new Label(parent, SWT.TOP);
		label.setText("Type: ");
		label.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		
		types = AnnotationPlugin.getManager().getAnnotationTargets(object);
		
		combo = new Table(parent, SWT.BORDER);
		combo.setHeaderVisible(false);
		combo.setLinesVisible(false);
		for (int i = 0; i < types.length; i++) {
			AnnotationType type = types[i].getType();
			TableItem item = new TableItem(combo, SWT.NONE);
			item.setText(type.getName());
			ILabelProvider provider = AnnotationUIPlugin.getManager(
				).getLabelProvider(type);
			if (provider != null) {
				try {
					Image image = provider.getImage(type.createInstance());
					item.setImage(image);
				}
				catch (Exception e) {
					AnnotationUIPlugin.log(e);
				}
			}
		}
		combo.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		comboListener = new ComboListener();
		combo.addSelectionListener(comboListener);
		if (combo.getItemCount() > 0)
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
			int index = combo.getSelectionIndex();
			if (index >= 0 && index < types.length) {
				type = types[combo.getSelectionIndex()];
				String d = type.getType().getDesciption();
				if (d == null) d = "";
				setMessage(d);
			}
		}
	
	}

}
