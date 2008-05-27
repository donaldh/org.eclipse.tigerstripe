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

import java.util.ArrayList;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.tigerstripe.annotation.core.ProviderContext;

/**
 * @author Yuri Strot
 *
 */
public class TypesControl extends Composite {
	
	private Map<Object, ProviderContext> map;
	private ArrayList<Object> collection = new ArrayList<Object>();
	private List list;
	private Object selected;

	/**
	 * @param parent
	 * @param style
	 */
	public TypesControl(Map<Object, ProviderContext> map, Composite parent, int style) {
		super(parent, style);
		this.map = map;
		init();
	}
	
	public void init() {
		setLayout(new GridLayout());
		Label label = new Label(this, SWT.WRAP);
		label.setText("This annotation can be added to selected object in the several ways. " +
				"Annotation should be added for this object as for: ");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		list = new List(this, SWT.MULTI | SWT.BORDER);
		for (Object object : map.keySet()) {
			collection.add(object);
			list.add(map.get(object).getTargetDescription());
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
				selected = collection.get(list.getSelectionIndex());
			}
		
		});
		list.select(0);
		selected = collection.get(0);
	}
	
	public Object getSelected() {
		return selected;
	}
	

}
