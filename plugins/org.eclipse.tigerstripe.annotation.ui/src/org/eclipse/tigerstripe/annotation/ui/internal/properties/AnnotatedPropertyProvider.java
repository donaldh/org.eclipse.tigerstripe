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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EProperty;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EPropertyProvider;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EditorProperty;
import org.eclipse.tigerstripe.annotation.ui.core.properties.MultilineProperty;

/**
 * @author Yuri Strot
 *
 */
public class AnnotatedPropertyProvider implements EPropertyProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.EPropertyProvider#getProperty(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public EProperty getProperty(EObject object, EStructuralFeature feature) {
		Class<?> clazz = feature.getEType().getInstanceClass();
		if (clazz != null && clazz.equals(String.class)) {
			String value = EditorProperty.getValue(feature, EditorProperty.ANNOTATION_EDITOR);
			if (EditorProperty.isCorrectValue(value))
				return new EditorProperty(object, feature);
			
			value = MultilineProperty.getValue(feature, MultilineProperty.ANNOTATION_MULTILINE);
			if (MultilineProperty.isCorrectValue(value))
				return new MultilineProperty(object, feature);
		}
		return null;
	}

}
