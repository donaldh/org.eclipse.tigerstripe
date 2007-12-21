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
package org.eclipse.tigerstripe.eclipse;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.tigerstripe.api.API;
import org.eclipse.tigerstripe.api.rendering.IDiagramRenderer;
import org.eclipse.tigerstripe.api.rendering.IDiagramRenderingSession;
import org.eclipse.tigerstripe.core.TigerstripeRuntime;
import org.osgi.framework.BundleContext;

public class BasePlugin extends Plugin {

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

		IDiagramRenderingSession session = API.getIDiagramRenderingSession();
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

}
