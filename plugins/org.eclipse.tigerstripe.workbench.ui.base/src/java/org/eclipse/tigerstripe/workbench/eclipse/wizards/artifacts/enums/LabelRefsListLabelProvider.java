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
package org.eclipse.tigerstripe.workbench.eclipse.wizards.artifacts.enums;

import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class LabelRefsListLabelProvider extends LabelProvider {

	private Image attributeImage;

	public LabelRefsListLabelProvider() {
		super();
		attributeImage = JavaPluginImages
				.get(JavaPluginImages.IMG_FIELD_DEFAULT);
	}

	@Override
	public Image getImage(Object element) {
		return attributeImage;
	}

	@Override
	public String getText(Object element) {
		if (element == null)
			return "unknown";

		LabelRef attributeRef = (LabelRef) element;
		return attributeRef.getName();
	}
}