/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.ui.core.view;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

public interface INoteProvider {

	public void setSelection(IWorkbenchPart part, ISelection selection);

	public boolean isNotable();

	public INote[] getNotes();

	public void addListener(INoteListener listener);

	public void removeListener(INoteListener listener);

	public void fillMenu(IMenuManager manager, String groupName, INote note);

}
