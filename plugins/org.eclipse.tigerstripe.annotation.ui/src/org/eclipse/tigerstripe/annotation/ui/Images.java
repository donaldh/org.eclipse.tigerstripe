/******************************************************************************* 
 * Copyright (c) 2010 xored software, Inc.  
 * 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html  
 * 
 * Contributors: 
 *     xored software, Inc. - initial API and Implementation (Yuri Strot) 
 *******************************************************************************/
package org.eclipse.tigerstripe.annotation.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

public class Images {

	private static final String PREFIX = "icons/";

	public static final String ADD = PREFIX + "add.gif";
	public static final String ANNOTATION = PREFIX + "annotation.gif";
	public static final String ANNOTATIONS = PREFIX + "annotations.gif";
	public static final String CANCEL = PREFIX + "cancel.gif";
	public static final String NEW_ANNOTATION = PREFIX + "new_annotation.gif";
	public static final String REFRESH = PREFIX + "refresh.gif";
	public static final String REMOVE = PREFIX + "remove.gif";
	public static final String REVERT_ALL = PREFIX + "revert_all.gif";
	public static final String REVERT = PREFIX + "revert.gif";
	public static final String SAVE_ALL = PREFIX + "save_all.gif";
	public static final String SAVE = PREFIX + "save.gif";
	public static final String UNKNOWN = PREFIX + "unknown.gif";

	public static Image getImage(String key) {
		ImageRegistry registry = AnnotationUIPlugin.getDefault()
				.getImageRegistry();
		Image image = registry.get(key);
		if (image == null) {
			ImageDescriptor descriptor = getDescriptor(key);
			image = descriptor.createImage();
			registry.put(key, image);
		}
		return image;
	}

	public static ImageDescriptor getDescriptor(String key) {
		return AnnotationUIPlugin.imageDescriptorFromPlugin(
				AnnotationUIPlugin.PLUGIN_ID, key);
	}

}
