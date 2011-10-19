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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.tigerstripe.annotation.internal.core.AnnotationManager;
import org.osgi.framework.BundleContext;

/**
 * The central class for access to the <code>IAnnotationManager</code>.
 * 
 * @see IAnnotationManager
 * @author Yuri Strot
 */
public class AnnotationPlugin extends Plugin {

	public static final String PLUGIN_ID = "org.eclipse.tigerstripe.annotation.core";

	private static AnnotationPlugin plugin;

	public AnnotationManager annotationManager;

	public AnnotationPlugin() {
		plugin = this;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		annotationManager = new AnnotationManager();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (annotationManager != null) {
			annotationManager.dispose();
		}
		super.stop(context);
	}

	public static IAnnotationManager getManager() {
		return plugin.annotationManager;
	}

	public static void warn(String message, Object...args) {
		if (plugin == null) {
			return;
		}
		plugin.getLog().log(
				new Status(IStatus.WARNING, getPluginId(), String.format(
						message, args)));
	}
	
	public static void log(Throwable t) {
		
		if (plugin == null) {
			return;
		}
		
		if (t instanceof CoreException) {
			plugin.getLog().log(((CoreException) t).getStatus());
		} else {
			String message = t.getMessage();
			if (message == null) {
				message = "";
			}
			plugin.getLog().log(
					new Status(IStatus.ERROR, getPluginId(), 0, message, t));
		}
	}

	public static String getPluginId() {
		return plugin.getBundle().getSymbolicName();
	}
	
	public static TransactionalEditingDomain getDomain() {
		return TransactionalEditingDomain.Registry.INSTANCE
				.getEditingDomain("org.eclipse.tigerstripe.AnnotationsDomain");
	}
}
