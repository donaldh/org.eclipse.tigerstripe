package org.eclipse.tigerstripe.espace.resources.monitor;

import org.eclipse.core.resources.ISaveParticipant;
import org.eclipse.core.resources.ISavedState;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ResourcesMonitorPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.tigerstripe.espace.resources.monitor";

	// The shared instance
	private static ResourcesMonitorPlugin plugin;

	/**
	 * The constructor
	 */
	public ResourcesMonitorPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		ISaveParticipant saveParticipant = new SaveParticipant();
		ISavedState lastState = ResourcesPlugin.getWorkspace()
				.addSaveParticipant(this, saveParticipant);
		if (lastState != null) {
			lastState.processResourceChangeEvents(ResourcesMonitor
					.getInstance());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
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
	public static ResourcesMonitorPlugin getDefault() {
		return plugin;
	}

}
