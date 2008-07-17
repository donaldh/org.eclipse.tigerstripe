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

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Yuri Strot
 *
 */
public abstract class AnnotatedProperty extends EPropertyImpl {
	
	private static final String ANNOTATION_MARKER = "org.eclipse.tigerstripe.annotation";

	/**
	 * @param object
	 * @param feature
	 */
	public AnnotatedProperty(EObject object, EStructuralFeature feature) {
		super(object, feature);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.AbstractProperty#getEditor(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public CellEditor createEditor(Composite parent) {
		String value = getValue(getDetailName());
		return createEditor(parent, value);
	}
	
	public static String getValue(EStructuralFeature feature, String detail) {
    	EAnnotation annotation = feature.getEAnnotation(ANNOTATION_MARKER);
    	if (annotation != null) {
			String value = annotation.getDetails().get(detail);
			return value;
    	}
    	return null;
	}
	
	protected abstract String getDetailName();
	
	protected CellEditor createEditor(Composite parent, String value) {
		return null;
	}
	
	protected String getValue(String detail) {
		return getValue(feature, detail);
	}

}
