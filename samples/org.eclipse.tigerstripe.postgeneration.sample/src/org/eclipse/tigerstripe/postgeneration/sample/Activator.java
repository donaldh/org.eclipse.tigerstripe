package org.eclipse.tigerstripe.postgeneration.sample;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.tigerstripe.postgeneration.sample"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
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
	public static Activator getDefault() {
		return plugin;
	}
	
	public static void log(IStatus status) {
		// add the status message to the "Problems" view
		getDefault().getLog().log(status);
	}
	
	public static void logErrorMessage(String message) {
		log(new Status(IStatus.ERROR, getPluginId(), message, null));
	}
	
	public static String getPluginId() {
		return PLUGIN_ID;
	}
	
	public static void logErrorMessage(String message, Throwable e) {
		log(new Status(IStatus.ERROR, getPluginId(), message, e));
	}

}
