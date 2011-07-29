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
package org.eclipse.tigerstripe.annotation.ui.core.properties;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * Annotation property. Clients should not implement this interface directly,
 * but should extend <code>EPropertyImpl</code> implementation. 
 * 
 * @author Yuri Strot
 */
public interface EProperty {
	
	/**
	 * @return property value
	 */
	public Object getValue();
	
	/**
	 * Set property value
	 * 
	 * @param value
	 */
	public void setValue(Object value);
	
	/**
	 * @return property name
	 */
	public String getName();
	
	/**
	 * @return human-readable value
	 */
	public String getDisplayName();
	
	/**
	 * save property value
	 */
	public void save();
	
	/**
	 * @return property type
	 */
	public EClassifier getEType();
	
	/**
	 * @param parent
	 * @return cell editor for this property or null if none
	 */
	public CellEditor getEditor(Composite parent);
	
	/**
	 * @return
	 */
	public IEditableValue getEditableValue();

	/**
	 * @return
	 */
	public String getDescription();

}
