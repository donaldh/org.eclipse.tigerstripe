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
package org.eclipse.tigerstripe.annotation.ui.internal.view.property;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EProperty;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EditableListValue;
import org.eclipse.tigerstripe.annotation.ui.core.properties.IEditableValue;

/**
 * @author Yuri Strot
 *
 */
public class PropertySelection {
	
	public static final int SINGLE_SELECTION = 0;
	
	public static final int PARENT_SELECTION = 1;
	
	public static final int CHILD_SELECTION = 2;
	
	private EProperty property;
	private TreeViewer viewer;
	
	public PropertySelection(EProperty property, TreeViewer viewer) {
		this.property = property;
		this.viewer = viewer;
	}
	
	public int getStatus() {
		IEditableValue value = property.getEditableValue();
		if (value instanceof EditableListValue)
			return CHILD_SELECTION;
		if (value.getValue() instanceof List<?>)
			return PARENT_SELECTION;
		return SINGLE_SELECTION;
	}
	
	@SuppressWarnings("unchecked")
	public void remove() {
		IEditableValue value = property.getEditableValue();
		if (value instanceof EditableListValue) {
			EditableListValue editableValue = (EditableListValue)value;
			editableValue.remove();
			viewer.refresh();
		}
		else if (value.getValue() instanceof List<?>) {
			List<Object> list = (List<Object>)value.getValue();
			list.clear();
			viewer.refresh();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void addDefaultValue() {
		IEditableValue value = property.getEditableValue();
		Object defaultValue = value.getDefaultValue();
		EClassifier classifier = value.getClassifier();
		if (classifier instanceof EClass) {
			EClass clazz = (EClass)classifier;
			defaultValue = clazz.getEPackage().getEFactoryInstance().create(clazz);
		}
		if (value instanceof EditableListValue) {
			EditableListValue editableValue = (EditableListValue)value;
			editableValue.insert(defaultValue);
			viewer.refresh();
		}
		else if (value.getValue() instanceof List<?>) {
			List<Object> list = (List<Object>)value.getValue();
			list.add(defaultValue);
			viewer.refresh();
		}
	}

}
