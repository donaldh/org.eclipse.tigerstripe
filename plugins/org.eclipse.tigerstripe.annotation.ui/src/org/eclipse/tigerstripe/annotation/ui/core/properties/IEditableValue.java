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

/**
 * @author Yuri Strot
 *
 */
public interface IEditableValue {
	
	public EClassifier getClassifier();
	
	public Object getValue();
	
	public void setValue(Object value);
	
	public String getName();
	
	public boolean isMany();
	
	public Object getDefaultValue();
	
	public void save();

	public String getDescription();

}
