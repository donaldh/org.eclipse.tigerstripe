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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;

/**
 * @author Yuri Strot
 *
 */
public class EditableFeature implements IEditableValue {
	
	private EObject object;
	private EStructuralFeature feature;
	
	public EditableFeature(EObject object, EStructuralFeature feature) {
		this.object = object;
		this.feature = feature;
	}
	
	/**
	 * @return the feature
	 */
	public EStructuralFeature getFeature() {
		return feature;
	}
	
	/**
	 * @return the object
	 */
	public EObject getObject() {
		return object;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.IEditableValue#getClassifier()
	 */
	public EClassifier getClassifier() {
		return feature.getEType();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.IEditableValue#getValue()
	 */
	public Object getValue() {
		return object.eGet(feature);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.IEditableValue#setValue(java.lang.Object)
	 */
	public void setValue(Object value) {
		object.eSet(feature, value);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.IEditableValue#getName()
	 */
	public String getName() {
		return feature.getName();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.IEditableValue#isMany()
	 */
	public boolean isMany() {
		return feature.isMany();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.IEditableValue#getDefaultValue()
	 */
	public Object getDefaultValue() {
		return feature.getDefaultValue();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.IEditableValue#save()
	 */
	public void save() {
		EObject current = object;
		while(current != null) {
			if (current instanceof Annotation) {
				Annotation annotation = (Annotation)current;
				AnnotationPlugin.getManager().save(annotation);
				return;
			}
			current = current.eContainer();
		}
	}

}
