package org.eclipse.tigerstripe.annotation.ui.diagrams;

import org.eclipse.tigerstripe.annotation.ui.internal.diagrams.DiagramService;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class AnnotationDiagramPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.tigerstripe.annotation.ui.diagrams";

	// The shared instance
	private static AnnotationDiagramPlugin plugin;
	
	private DiagramService service;
	
	/**
	 * The constructor
	 */
	public AnnotationDiagramPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		service = new DiagramService();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		service.dispose();
		service = null;
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static AnnotationDiagramPlugin getDefault() {
		return plugin;
	}
	
	public static IDiagramService getService() {
		return plugin.service;
	}

}
