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
package org.eclipse.tigerstripe.annotation.ui.internal.view;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Yuri Strot
 *
 */
public interface IProperty {
	
	public String getName();
	
	public Object getValue();
	
	public String getDisplayName();
	
	public EClassifier getEType();
	
	public void setValue(Object value);
	
	public CellEditor getEditor(Composite parent);
	
	public void applyEditorValue();

}
