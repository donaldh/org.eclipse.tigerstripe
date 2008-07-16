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
package org.eclipse.tigerstripe.annotation.ui.internal.properties;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.ui.celleditor.ExtendedDialogCellEditor;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Yuri Strot
 *
 */
public class StringListCellEditor extends ExtendedDialogCellEditor {
	
	private String name;
	private List<String> values;
	private boolean valueChanged;

	public StringListCellEditor(Composite composite, List<String> values, String name) {
		super(composite, new DefaultLabelProvider());
		this.values = values;
		this.name = name;
		this.valueChanged = false;
	}
	
	protected Object openDialogBox(Control cellEditorWindow) {
		Shell shell = cellEditorWindow.getShell();
		ChangeListDialog dialog = new ChangeListDialog(shell,
												toWrapper(values), name);
		dialog.open();
		valueChanged = true;
		return toString(dialog.getResult());
	}
    
    @Override
	protected Object doGetValue() {
    	if (valueChanged)
    		return super.doGetValue();
    	return values;
	}
    
	private List<String> toString(List<StringWrapper> list) {
		List<String> result = new BasicEList<String>();
		if (list != null) {
			Iterator<StringWrapper> it = list.iterator();
			while (it.hasNext()) {
				StringWrapper wrapper = (StringWrapper) it.next();
				result.add(wrapper.getString());
			}
		}
		return result;
	}
    
	private List<StringWrapper> toWrapper(List<String> list) {
		List<StringWrapper> result = new ArrayList<StringWrapper>();
		if (list != null) {
			Iterator<String> it = list.iterator();
			while (it.hasNext()) {
				String elem = (String) it.next();
				StringWrapper wrapper = new StringWrapper();
				wrapper.setString(elem);
				result.add(wrapper);
			}
		}
		return result;
	}

}
