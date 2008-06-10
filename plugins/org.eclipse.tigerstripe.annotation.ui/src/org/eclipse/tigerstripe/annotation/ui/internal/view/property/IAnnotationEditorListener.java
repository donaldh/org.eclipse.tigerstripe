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

/**
 * @author Yuri Strot
 *
 */
public interface IAnnotationEditorListener {
	
	public static final int NO_CHANGES = 0;
	
	public static final int NON_SELECTION_CHANGES = 1;
	
	public static final int SELECTION_CHANGES = 2;
	
	public void dirtyChanged(int status);

}
