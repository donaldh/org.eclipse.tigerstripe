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
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.tigerstripe.annotation.core.util.ObjectUtil;
import org.eclipse.tigerstripe.annotation.ui.util.DisplayAnnotationUtil;

/**
 * Default implementation of the <code>EProperty</code>
 * 
 * @author Yuri Strot
 */
public class EPropertyImpl implements EProperty {

	protected CellEditor cellEditor;
	protected IEditableValue value;

	public EPropertyImpl(IEditableValue value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.tigerstripe.annotation.ui.core.properties.EProperty#
	 * getEditableValue()
	 */
	public IEditableValue getEditableValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.annotation.ui.core.EProperty#getDisplayName()
	 */
	public String getDisplayName() {
		return getValueDisplayName(getValue());
	}

	protected String getValueDisplayName(Object value) {
		if (value instanceof List<?>) {
			List<?> list = (List<?>) value;
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
			return DisplayAnnotationUtil.getText((EObject) value);
		}
		String text = value.toString();
		if (text == null)
			text = "";
		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.tigerstripe.annotation.ui.core.EProperty#getEType()
	 */
	public EClassifier getEType() {
		return value.getClassifier();
	}

	protected CellEditor createEditor(Composite parent) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.annotation.ui.core.EProperty#getEditor(org.eclipse
	 * .swt.widgets.Composite)
	 */
	public final CellEditor getEditor(Composite parent) {
		cellEditor = createEditor(parent);
		return cellEditor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.tigerstripe.annotation.ui.core.EProperty#getName()
	 */
	public String getName() {
		return value.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.tigerstripe.annotation.ui.core.EProperty#getValue()
	 */
	public Object getValue() {
		return value.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.tigerstripe.annotation.ui.core.EProperty#save()
	 */
	public void save() {
		value.save();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.tigerstripe.annotation.ui.core.EProperty#setValue(java.lang
	 * .Object)
	 */
	public void setValue(Object value) {
		this.value.setValue(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EPropertyImpl) {
			EPropertyImpl property = (EPropertyImpl) obj;
			return ObjectUtil.equals(value, property.value);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return ObjectUtil.hashCode(value);
	}

}
