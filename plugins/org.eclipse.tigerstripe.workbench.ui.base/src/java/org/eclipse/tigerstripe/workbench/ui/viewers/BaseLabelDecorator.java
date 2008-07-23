/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.viewers;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IModelComponent;

public abstract class BaseLabelDecorator implements ITigerstripeLabelDecorator {

	public String decorateText(String text, Object element) {
		IModelComponent component = getModelComponent(element);
		if (component != null) {
			return decorateText(text, component);
		}
		return text;
	}

	public Image decorateImage(Image image, Object element) {
		IModelComponent component = getModelComponent(element);
		if (component != null) {
			return decorateImage(image, component);
		}
		return image;
	}

	public void addListener(ILabelProviderListener listener) {
	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
	}

	protected IModelComponent getModelComponent(Object element) {
		if (element instanceof IAdaptable) {
			IAdaptable ad = (IAdaptable) element;
			return (IModelComponent) ad.getAdapter(IModelComponent.class);
		}
		return null;
	}
}
