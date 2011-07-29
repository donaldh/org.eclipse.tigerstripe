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

import java.util.List;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.tigerstripe.annotation.core.util.ObjectUtil;

/**
 * @author Yuri Strot
 *
 */
public class EditableListValue implements IEditableValue {
	
	private List<Object> list;
	private int index;
	private EditableFeature parent;
	
	public EditableListValue(EditableFeature parent, List<Object> list, int index) {
		this.list = list;
		this.index = index;
		this.parent = parent;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EditableListValue) {
			EditableListValue value = (EditableListValue)obj;
			return ObjectUtil.equals(parent, value.parent) && 
				ObjectUtil.equals(list, value.list) && index == value.index;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return index ^ ObjectUtil.hashCode(list) ^ ObjectUtil.hashCode(parent);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.IEditableValue#getClassifier()
	 */
	public EClassifier getClassifier() {
		return parent.getFeature().getEType();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.IEditableValue#getDefaultValue()
	 */
	public Object getDefaultValue() {
		return parent.getFeature().getDefaultValue();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.IEditableValue#getName()
	 */
	public String getName() {
		return getClassifier().getName();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.IEditableValue#getValue()
	 */
	public Object getValue() {
		return list.get(index);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.IEditableValue#isMany()
	 */
	public boolean isMany() {
		return (getValue() instanceof List<?>);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.IEditableValue#setValue(java.lang.Object)
	 */
	public void setValue(Object value) {
		if (value != null)
			list.set(index, value);
		else
			remove();
	}
	
	public void insert(Object value) {
		if (value != null)
			list.add(index, value);
	}
	
	public void remove() {
		list.remove(index);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.tigerstripe.annotation.ui.core.properties.IEditableValue#save()
	 */
	public void save() {
		parent.save();
	}

	public String getDescription() {
		return null;
	}

}
