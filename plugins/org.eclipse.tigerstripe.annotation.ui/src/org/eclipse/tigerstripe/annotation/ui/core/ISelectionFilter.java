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
 * Interface for filters workbench selection
 * 
 * @author Yuri Strot
 */
public interface ISelectionFilter {
	
	/**
	 * Determines if this part and selection pass this filter.
	 * 
	 * @param part workbench part with the selection
	 * @param selection
	 * @return <code>true</code> if part and selection is accepted by the filter.
	 */
	public boolean select(IWorkbenchPart part, ISelection selection);

}
