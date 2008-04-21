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
package org.eclipse.tigerstripe.annotation.ui.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Yuri Strot
 *
 */
public class CompositeSelectionFilter implements ISelectionFilter {
	
	List<ISelectionFilter> filters = new ArrayList<ISelectionFilter>();
	
	public void addFilter(ISelectionFilter filter) {
		filters.add(filter);
	}
	
	public void removeFilter(ISelectionFilter filter) {
		filters.remove(filter);
	}

	public boolean select(IWorkbenchPart part, ISelection selection) {
		Iterator<ISelectionFilter> it = filters.iterator();
		while (it.hasNext()) {
	        ISelectionFilter filter = it.next();
	        if (!filter.select(part, selection))
	        	return false;;
        }
	    return true;
    }

}
