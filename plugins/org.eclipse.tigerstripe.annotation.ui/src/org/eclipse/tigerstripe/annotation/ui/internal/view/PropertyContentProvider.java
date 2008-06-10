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
package org.eclipse.tigerstripe.annotation.ui.internal.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * @author Yuri Strot
 *
 */
public class PropertyContentProvider implements ITreeContentProvider {

	public void dispose() {
    }

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

	public Object[] getElements(Object inputElement) {
		List<IProperty> children = new ArrayList<IProperty>();
		EObject object = null;
		if (inputElement instanceof EObject) {
			object = (EObject)inputElement;
		}
		if (inputElement instanceof IProperty) {
			IProperty property = (IProperty)inputElement;
			if (property.getEType() instanceof EClass) {
				object = (EObject)property.getValue();
			}
		}
		if (object != null) {
			Iterator<EStructuralFeature> attrs = object.eClass().getEAllStructuralFeatures().iterator();
			while (attrs.hasNext()) {
				EStructuralFeature feature = (EStructuralFeature) attrs.next();
				children.add(new EProperty(object, feature));
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

	public boolean hasChildren(Object element) {
		EObject object = null;
		if (element instanceof EObject) {
			object = (EObject)element;
		}
		if (element instanceof IProperty) {
			IProperty property = (IProperty)element;
			if (property.getValue() instanceof EObject) {
				object = (EObject)property.getValue();
			}
		}
		if (object != null) {
			return object.eClass().getEAllStructuralFeatures().size() > 0;
		}
		return false;
	}

}
