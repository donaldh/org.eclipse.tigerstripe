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

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.annotation.core.Annotation;
import org.eclipse.tigerstripe.annotation.core.AnnotationPlugin;
import org.eclipse.tigerstripe.annotation.ui.util.DisplayAnnotationUtil;

/**
 * Default implementation of the <code>EProperty</code>
 * 
 * @author Yuri Strot
 */
public class EPropertyImpl implements EProperty {
	
	protected EObject object;
	protected EStructuralFeature feature;
	protected CellEditor cellEditor;
	
	public EPropertyImpl(EObject object, EStructuralFeature feature) {
		this.object = object;
		this.feature = feature;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.EProperty#applyEditorValue()
	 */
	public void applyEditorValue() {
		if (cellEditor != null)
			setValue(cellEditor.getValue());
    }
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.EProperty#getDisplayName()
	 */
	public String getDisplayName() {
		return getValueDisplayName(getValue());
	}
	
	protected String getValueDisplayName(Object value) {
		if (value instanceof List<?>) {
			List<?> list = (List<?>)value;
			Iterator<?> it = list.iterator();
			StringBuffer buffer = new StringBuffer();
			if (it.hasNext())
				buffer.append(getValueDisplayName(it.next()));
			while (it.hasNext()) {
				buffer.append(", ");
				buffer.append(getValueDisplayName(it.next()));
			}
			return buffer.toString();
		}
		if (value == null)
			return "";
		if (value instanceof EObject) {
			return DisplayAnnotationUtil.getText((EObject)value);
		}
		String text = value.toString();
		if (text == null) text = "";
		if (text.length() > 35)
			text = text.substring(0, 33) + "...";
		return text;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.EProperty#getEType()
	 */
	public EClassifier getEType() {
		return feature.getEType();
	}
	
	protected CellEditor createEditor(Composite parent) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.EProperty#getEditor(org.eclipse.swt.widgets.Composite)
	 */
	public final CellEditor getEditor(Composite parent) {
		cellEditor = createEditor(parent);
		return cellEditor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.EProperty#getName()
	 */
	public String getName() {
	    return feature.getName();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.EProperty#getValue()
	 */
	public Object getValue() {
		return object.eGet(feature);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.EProperty#save()
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

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.EProperty#setValue(java.lang.Object)
	 */
	public void setValue(Object value) {
		object.eSet(feature, value);
	}

}
