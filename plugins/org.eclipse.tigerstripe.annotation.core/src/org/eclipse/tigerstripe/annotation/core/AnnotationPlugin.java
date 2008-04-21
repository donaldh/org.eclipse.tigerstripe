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
package org.eclipse.tigerstripe.annotation.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.annotation.internal.core.AnnotationManager;
import org.osgi.framework.BundleContext;

/**
 * The central class for access to the <code>IAnnotationManager</code>.
 * 
 * @see IAnnotationManager
 */
public class AnnotationPlugin extends Plugin {
	
	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.tigerstripe.annotation";

	// The shared instance
	private static AnnotationPlugin plugin;
	
	private AnnotationManager manager;
	
	/**
	 * The constructor
	 */
	public AnnotationPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		manager = AnnotationManager.getInstance();
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
	public static AnnotationPlugin getDefault() {
		return plugin;
	}
	
	public static void log(Throwable t) {
		String message = t.getMessage();
		if (message == null)
			message = "";
		plugin.getLog().log(new Status(IStatus.ERROR, PLUGIN_ID,0, message, t));
	}
	
	public static IAnnotationManager getManager() {
		return plugin.manager;
	}

}
