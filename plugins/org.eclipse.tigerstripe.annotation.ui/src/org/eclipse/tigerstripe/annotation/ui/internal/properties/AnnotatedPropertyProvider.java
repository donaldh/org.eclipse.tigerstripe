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

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EProperty;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EPropertyProvider;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EditableFeature;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EditorProperty;
import org.eclipse.tigerstripe.annotation.ui.core.properties.IEditableValue;
import org.eclipse.tigerstripe.annotation.ui.core.properties.MultilineProperty;

/**
 * @author Yuri Strot
 *
 */
public class AnnotatedPropertyProvider implements EPropertyProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.EPropertyProvider#getProperty(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EStructuralFeature)
	 */
	public EProperty getProperty(IEditableValue value) {
		if (value instanceof EditableFeature) {
			EditableFeature editableFeature = (EditableFeature)value;
			EStructuralFeature feature = editableFeature.getFeature();
			Class<?> clazz = feature.getEType().getInstanceClass();
			if (!feature.isMany() && clazz != null && clazz.equals(String.class)) {
				String sValue = EditorProperty.getValue(feature, EditorProperty.ANNOTATION_EDITOR);
				if (EditorProperty.isCorrectValue(sValue))
					return new EditorProperty(editableFeature);
				
				sValue = MultilineProperty.getValue(feature, MultilineProperty.ANNOTATION_MULTILINE);
				if (MultilineProperty.isCorrectValue(sValue))
					return new MultilineProperty(editableFeature);
			}
		}
		return null;
	}

}
