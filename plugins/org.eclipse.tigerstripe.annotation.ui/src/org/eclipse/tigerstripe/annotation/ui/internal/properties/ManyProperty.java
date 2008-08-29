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

import org.eclipse.tigerstripe.annotation.ui.core.properties.EPropertyImpl;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EditableFeature;

/**
 * @author Yuri Strot
 *
 */
public class ManyProperty extends EPropertyImpl {

	/**
	 * @param object
	 * @param feature
	 */
	public ManyProperty(EditableFeature value) {
		super(value);
	}
	
	public EditableFeature getEditableValue() {
		return (EditableFeature)value;
	}

}
