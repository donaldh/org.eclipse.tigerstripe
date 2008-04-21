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
package org.eclipse.tigerstripe.annotation.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.tigerstripe.annotation.ui.core.IAnnotationUIManager;
import org.eclipse.tigerstripe.annotation.ui.internal.core.AnnotationUIManager;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * This class should be used for access <code>IAnnotationUIManager</code>
 *
 * @see IAnnotationUIManager
 */
public class AnnotationUIPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.tigerstripe.annotation.ui";

	// The shared instance
	private static AnnotationUIPlugin plugin;
	
	private AnnotationUIManager manager;
	
	/**
	 * The constructor
	 */
	public AnnotationUIPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		manager = AnnotationUIManager.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static AnnotationUIPlugin getDefault() {
		return plugin;
	}
	
	public static IAnnotationUIManager getManager() {
		return plugin.manager;
	}
	
	public static Image createImage(String path) {
		ImageDescriptor descriptor = createImageDescriptor(path);
		return descriptor == null ? null : descriptor.createImage();
	}
	
	public static ImageDescriptor createImageDescriptor(String path) {
		 return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	public static void log(Throwable t) {
		String message = t.getMessage();
		if (message == null)
			message = "";
		plugin.getLog().log(new Status(IStatus.ERROR, PLUGIN_ID,0, message, t));
	}

}
