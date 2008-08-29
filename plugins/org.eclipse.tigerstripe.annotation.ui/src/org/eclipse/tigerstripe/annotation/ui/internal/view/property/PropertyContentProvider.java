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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EProperty;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EditableFeature;
import org.eclipse.tigerstripe.annotation.ui.core.properties.EditableListValue;
import org.eclipse.tigerstripe.annotation.ui.internal.properties.ManyProperty;
import org.eclipse.tigerstripe.annotation.ui.internal.properties.PropertyRegistry;

/**
 * @author Yuri Strot
 *
 */
public class PropertyContentProvider implements ITreeContentProvider {

	public void dispose() {
    }

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

	@SuppressWarnings("unchecked")
	public Object[] getElements(Object inputElement) {
		List<EProperty> children = new ArrayList<EProperty>();
		EObject object = null;
		if (inputElement instanceof EObject) {
			object = (EObject)inputElement;
		}
		if (inputElement instanceof EProperty) {
			EProperty property = (EProperty)inputElement;
			if (property.getEType() instanceof EClass && 
					!property.getEditableValue().isMany()) {
				object = (EObject)property.getValue();
			}
		}
		if (inputElement instanceof ManyProperty) {
			ManyProperty property = (ManyProperty)inputElement;
			EditableFeature value = property.getEditableValue();
			List<Object> list = (List<Object>)value.getValue();
			for (int i = 0; i < list.size(); i++) {
				EditableListValue ef = new EditableListValue(value, list, i);
				children.add(PropertyRegistry.getProperty(ef));
			}
		}
		else if (object != null) {
			Iterator<EStructuralFeature> attrs = object.eClass().getEAllStructuralFeatures().iterator();
			while (attrs.hasNext()) {
				EStructuralFeature feature = (EStructuralFeature) attrs.next();
				EditableFeature ef = new EditableFeature(object, feature);
				if (feature.isMany()) {
					children.add(new ManyProperty(ef));
				}
				else {
					children.add(PropertyRegistry.getProperty(ef));
				}
			}
		}
	    return children.toArray(new EProperty[children.size()]);
    }

	public Object[] getChildren(Object parentElement) {
		return getElements(parentElement);
	}

	public Object getParent(Object element) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public boolean hasChildren(Object element) {
		EObject object = null;
		if (element instanceof EObject) {
			object = (EObject)element;
		}
		if (element instanceof EProperty) {
			EProperty property = (EProperty)element;
			if (property.getValue() instanceof EObject) {
				object = (EObject)property.getValue();
			}
		}
		if (element instanceof ManyProperty) {
			ManyProperty property = (ManyProperty)element;
			EditableFeature value = property.getEditableValue();
			List<Object> list = (List<Object>)value.getValue();
			return list.size() > 0;
		}
		if (object != null) {
			return object.eClass().getEAllStructuralFeatures().size() > 0;
		}
		return false;
	}

}
