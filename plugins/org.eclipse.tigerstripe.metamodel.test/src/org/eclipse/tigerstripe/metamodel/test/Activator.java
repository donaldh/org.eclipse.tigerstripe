package org.eclipse.tigerstripe.metamodel.test;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.tigerstripe.test_common.BundleUtils;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	private BundleUtils bundleUtils = null;

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.tigerstripe.metamodel.test";

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
		bundleUtils = new BundleUtils();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		bundleUtils.setBundleRoot(context, PLUGIN_ID);
	}

	/*
	 * (non-Javadoc)
	 * 
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
	public static Activator getDefault() {
		return plugin;
	}

	public BundleUtils getBundleUtils() {
		return this.bundleUtils;
	}

}
