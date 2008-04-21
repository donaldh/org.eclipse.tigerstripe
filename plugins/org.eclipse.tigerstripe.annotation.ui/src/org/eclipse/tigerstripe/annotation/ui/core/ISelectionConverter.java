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

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

/**
 * An selection converter class provide a way to convert some selection to another.
 * For example, Java Editor provide ITextSelection (that can not be annotated) can be converter to the
 * IJavaElement (that can be annotated). So, we can listen java element selection directly from the
 * Java Editor. <code>ISelectionConverter</code> also provide a way to open selection in the workbench. 
 * Selection converter should be registered with
 * the <code>org.eclipse.tigerstripe.annotation.ui.workbenchAnnotationProvider</code>
 * extension point.
 * 
 * @author Yuri Strot
 */
public interface ISelectionConverter {
	
	/**
	 * Convert not annotable selection to the annotable selection. 
	 * 
	 * @param part
	 * @param selection
	 * @return
	 */
	public ISelection convert(IWorkbenchPart part, ISelection selection);
	
	/**
	 * Open selection in the workbench
	 * 
	 * @param selection
	 */
	public void open(ISelection selection);

}
