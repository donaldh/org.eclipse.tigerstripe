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
package org.eclipse.tigerstripe.annotation.ui.internal.actions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.tigerstripe.annotation.core.IAnnotationTarget;
import org.eclipse.tigerstripe.annotation.core.TargetAnnotationType;

/**
 * @author Yuri Strot
 *
 */
public class TypesControl extends Composite {
	
	private TargetAnnotationType targetType;
	private List list;
	private Object selected;

	/**
	 * @param parent
	 * @param style
	 */
	public TypesControl(TargetAnnotationType targetType, Composite parent, int style) {
		super(parent, style);
		this.targetType = targetType;
		init();
	}
	
	public void init() {
		setLayout(new GridLayout());
		Label label = new Label(this, SWT.WRAP);
		label.setText("This annotation can be added to selected object in the several ways. " +
				"Annotation should be added for this object as for: ");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		list = new List(this, SWT.MULTI | SWT.BORDER);
		
		for (IAnnotationTarget target : targetType.getTargets()) {
			list.add(target.getDescription());
		}
		list.setLayoutData(new GridData(GridData.FILL_BOTH));
		list.addSelectionListener(new SelectionListener() {
		
			public void widgetSelected(SelectionEvent e) {
				updateSelection();
			}
		
			public void widgetDefaultSelected(SelectionEvent e) {
				updateSelection();
			}
			
			public void updateSelection() {
				int index = list.getSelectionIndex();
				if (index >= 0) {
					IAnnotationTarget[] targets = targetType.getTargets();
					selected =  targets[index].getAdaptedObject();
				}
			}
		
		});
		list.select(0);
		selected = targetType.getTargets()[0].getAdaptedObject();
	}
	
	public Object getSelected() {
		return selected;
	}
	

}
