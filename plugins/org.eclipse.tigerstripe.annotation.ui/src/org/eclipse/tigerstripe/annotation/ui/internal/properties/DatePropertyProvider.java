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

import java.util.Date;

import org.eclipse.tigerstripe.annotation.ui.core.properties.DateProperty;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EProperty;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EPropertyProvider;
import org.eclipse.tigerstripe.annotation.ui.core.properties.IEditableValue;

/**
 * @author Yuri Strot
 *
 */
public class DatePropertyProvider implements EPropertyProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.EPropertyProvider#getProperty(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public EProperty getProperty(IEditableValue value) {
		Class<?> clazz = value.getClassifier().getInstanceClass();
		if (!value.isMany() && clazz != null && Date.class.equals(clazz)) {
			return new DateProperty(value);
		}
		return null;
	}

}
