/*
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *     E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 */
package org.eclipse.tigerstripe.workbench.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.rendering.IDiagramRenderer;
import org.eclipse.tigerstripe.workbench.internal.api.rendering.IDiagramRenderingSession;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.osgi.framework.BundleContext;

public class BasePlugin extends Plugin {

	public final static String PLUGIN_ID = "org.eclipse.tigerstripe.eclipse.BasePlugin";

	// The shared instance.
	private static BasePlugin plugin;

	private Location installLocation;

	private String tigerstripeRuntimeDir;
	
	public BasePlugin() {
		super();
		plugin = this;
	}

	public static BasePlugin getDefault() {
		return plugin;
	}

	public static String getPluginId() {
		return PLUGIN_ID;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		installLocation = Platform.getInstallLocation();
		String installationPath = installLocation.getURL().getPath();

		if (installationPath != null && installationPath.length() != 0) {
			tigerstripeRuntimeDir = installationPath + "/"
					+ TigerstripeRuntime.TIGERSTRIPE_HOME_DIR;
			TigerstripeRuntime.setTigerstripeRuntimeRoot(tigerstripeRuntimeDir);
			TigerstripeRuntime.initLogger();
		}

		extensionPointRegistered();
	}

	private void extensionPointRegistered() {

		IDiagramRenderingSession session = InternalTigerstripeCore
				.getIDiagramRenderingSession();
		IExtension[] extensions = BasePlugin.getDefault().getDescriptor()
				.getExtensionPoint("diagramRendering").getExtensions();

		for (IExtension extension : extensions) {
			IConfigurationElement[] configElements = extension
					.getConfigurationElements();
			for (IConfigurationElement configElement : configElements) {
				String actionClass = configElement
						.getAttribute("rendererClass");
				try {
					IDiagramRenderer renderer = (IDiagramRenderer) configElement
							.createExecutableExtension("renderClass");
					session.registerRenderer(renderer);
				} catch (CoreException e) {
					TigerstripeRuntime.logErrorMessage(
							"CoreException detected", e);
				}
			}
		}

	}

	public static void log(Throwable e) {
		if (e.getCause() == null) {
			IStatus status = new Status(IStatus.ERROR, getPluginId(),
					IStatus.ERROR, "Internal Error", e); //$NON-NLS-1$
			log(status);
			return;
		} else {
			MultiStatus mStatus = new MultiStatus(getPluginId(), IStatus.ERROR,
					"Internal Error", e);
			Throwable ee = e.getCause();

			while (ee != null) {
				IStatus subStatus = new Status(IStatus.ERROR, getPluginId(),
						IStatus.ERROR, "Internal Error", ee); //$NON-NLS-1$
				mStatus.add(subStatus);
				if (ee instanceof TigerstripeException) {
					ee = ((TigerstripeException) ee).getException();
				} else if (e.getCause() instanceof Exception) {
					ee = ee.getCause();
				} else {
					break;
				}
			}
			log(mStatus);
			return;
		}
	}

	public static void log(IStatus status) {
		// add the status message to the "Problems" view
		getDefault().getLog().log(status);
	}
}
