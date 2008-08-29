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

import org.eclipse.core.runtime.ListenerList;

/**
 * @author Yuri Strot
 *
 */
public class PropertiesSelectionManager {
	
	private static PropertiesSelectionManager INSTANCE;
	
	private PropertySelection selection;
	
	private ListenerList listeners;
	
	private PropertiesSelectionManager() {
		listeners = new ListenerList();
	}
	
	public static final PropertiesSelectionManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PropertiesSelectionManager();
		}
		return INSTANCE;
	}
	
	public void addListener(IPropertiesSelectionListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(IPropertiesSelectionListener listener) {
		listeners.remove(listener);
	}
	
	public void setSelection(PropertySelection selection) {
		this.selection = selection;
		fireSelectionChanged();
	}
	
	protected void fireSelectionChanged() {
		Object[] objects = listeners.getListeners();
		for (int i = 0; i < objects.length; i++) {
			IPropertiesSelectionListener listener = (IPropertiesSelectionListener)objects[i];
			listener.selectionChanged(selection);
		}
	}
	
	/**
	 * @return the index
	 */
	public PropertySelection getSelection() {
		return selection;
	}

}
