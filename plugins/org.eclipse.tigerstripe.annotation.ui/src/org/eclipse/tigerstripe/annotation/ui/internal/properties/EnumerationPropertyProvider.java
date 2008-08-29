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

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EProperty;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EPropertyProvider;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EnumerationProperty;
import org.eclipse.tigerstripe.annotation.ui.core.properties.IEditableValue;

/**
 * @author Yuri Strot
 *
 */
public class EnumerationPropertyProvider implements EPropertyProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.EPropertyProvider#getProperty(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public EProperty getProperty(IEditableValue value) {
		if (!value.isMany() && value.getClassifier() instanceof EDataType) {
			EDataType type = (EDataType)value.getClassifier();
			if (!isEnumerationFacetEmpty(type) || type instanceof EEnum) {
				return new EnumerationProperty(value);
			}
		}
		return null;
	}
	
	protected boolean isEnumerationFacetEmpty(EDataType type) {
		return ExtendedMetaData.INSTANCE.getEnumerationFacet(type).isEmpty();
	}

}
