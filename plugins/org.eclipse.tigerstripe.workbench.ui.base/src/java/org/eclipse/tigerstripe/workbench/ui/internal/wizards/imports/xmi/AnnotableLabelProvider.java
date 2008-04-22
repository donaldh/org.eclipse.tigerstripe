/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.internal.wizards.imports.xmi;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.internal.core.model.importing.AnnotableElement;
import org.eclipse.tigerstripe.workbench.ui.internal.resources.Images;

public class AnnotableLabelProvider extends LabelProvider {

	public AnnotableLabelProvider() {
		super();
	}

	public String getName(Object element) {
		if (element == null)
			return "";

		AnnotatedElement annotatedElement = (AnnotatedElement) element;
		return annotatedElement.getName();
	}

	public Object getProperty(Object Element, String propertyName) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setProperty(Object Element, String propertyName, Object property) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image getImage(Object element) {
		AnnotableElement annotableElement = (AnnotableElement) element;

		if (AnnotableElement.AS_DATATYPE.equals(annotableElement
				.getAnnotationType()))
			return Images.get(Images.DATATYPE_ICON);
		if (AnnotableElement.AS_ENTITY.equals(annotableElement
				.getAnnotationType()))
			return Images.get(Images.ENTITY_ICON);
		if (AnnotableElement.AS_ENUMERATION.equals(annotableElement
				.getAnnotationType()))
			return Images.get(Images.ENUM_ICON);

		return null;
	}

	public String getPackage(Object element) {
		AnnotableElement annotableElement = (AnnotableElement) element;
		if (annotableElement == null
				|| annotableElement.getAnnotableElementPackage() == null)
			return "";

		return annotableElement.getAnnotableElementPackage()
				.getFullyQualifiedName();
	}

	@Override
	public String getText(Object element) {
		if (element == null)
			return "unknown";

		AnnotableElement annotableElement = (AnnotableElement) element;
		return annotableElement.getFullyQualifiedName();
	}
}
