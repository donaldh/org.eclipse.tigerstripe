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
package org.eclipse.tigerstripe.workbench.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.workbench.ui.rendererplugin.Activator;

public class Images {

	private static final String PREFIX = "icons/";

	public static final String FILL_COLOR_ENABLED = PREFIX
			+ "fill_color_enabled.gif";
	public static final String FILL_COLOR_DISABLED = PREFIX
			+ "fill_color_disabled.gif";

	public static Image getImage(String key) {
		ImageRegistry registry = Activator.getDefault().getImageRegistry();
		Image image = registry.get(key);
		if (image == null) {
			ImageDescriptor descriptor = getDescriptor(key);
			image = descriptor.createImage();
			registry.put(key, image);
		}
		return image;
	}

	public static ImageDescriptor getDescriptor(String key) {
		return Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, key);
	}
}
