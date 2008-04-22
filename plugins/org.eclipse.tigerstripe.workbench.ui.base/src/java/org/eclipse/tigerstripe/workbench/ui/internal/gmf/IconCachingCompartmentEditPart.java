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
package org.eclipse.tigerstripe.workbench.ui.internal.gmf;

import java.util.HashMap;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.diagram.ui.editparts.CompartmentEditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * Bug 931: after a while Eclipse runs out of handles.
 * 
 * This ensures that the icon Image is cached and NOT recreated everytime.
 * 
 * @author Eric Dillon
 * 
 */
public class IconCachingCompartmentEditPart extends CompartmentEditPart {

	private static HashMap<ImageDescriptor, Image> iconRegistry = new HashMap<ImageDescriptor, Image>();

	public IconCachingCompartmentEditPart(EObject model) {
		super(model);
	}

	protected Image getCachedLabelIcon(ImageDescriptor descriptor) {
		// Bug 931: Avoid recreating images everytime
		Image cachedIcon = iconRegistry.get(descriptor);
		if (cachedIcon == null) {
			cachedIcon = descriptor.createImage();
			iconRegistry.put(descriptor, cachedIcon);
		}
		return cachedIcon;
	}
}
