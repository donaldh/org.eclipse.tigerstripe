/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     xored software, Inc. - initial API and implementation (Yuri Strot)
 ******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.dependencies.internal.diagram.ui;

import java.net.URL;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class DependencyDiagramPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.tigerstripe.dependencies";

	// The shared instance
	private static DependencyDiagramPlugin plugin;

	/**
	 * The constructor
	 */
	public DependencyDiagramPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static DependencyDiagramPlugin getDefault() {
		return plugin;
	}

	public static Image getImage(URL url) {
		if (url == null)
			return null;
		String key = url.toString();
		Image image = getDefault().getImageRegistry().get(key);
		if (image == null) {
			ImageDescriptor descriptor = ImageDescriptor.createFromURL(url);
			image = descriptor.createImage();
			getDefault().getImageRegistry().put(key, image);
		}
		return image;
	}

	public static void log(Throwable t) {
		getDefault().getLog().log(
				new Status(IStatus.ERROR, PLUGIN_ID, t.getMessage(), t));
	}

}
