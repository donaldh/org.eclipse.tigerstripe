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

import org.apache.log4j.Level;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.internal.api.rendering.IDiagramRenderer;
import org.eclipse.tigerstripe.workbench.internal.api.rendering.IDiagramRenderingSession;
import org.eclipse.tigerstripe.workbench.internal.builder.WorkspaceListener;
import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;
import org.eclipse.tigerstripe.workbench.internal.startup.PostInstallActions;
import org.osgi.framework.BundleContext;

public class BasePlugin extends Plugin {

	public final static String PLUGIN_ID = "org.eclipse.tigerstripe.eclipse.BasePlugin";

	// The shared instance.
	private static BasePlugin plugin;

	private WorkspaceListener listener;

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

		executePostInstallationActions(context);

		extensionPointRegistered();

		startWorkspaceListener();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);

		stopWorkspaceListener();
	}

	private void extensionPointRegistered() {

		IDiagramRenderingSession session = InternalTigerstripeCore
				.getIDiagramRenderingSession();
		IConfigurationElement[] configElements = Platform
				.getExtensionRegistry()
				.getConfigurationElementsFor(
						"org.eclipse.tigerstripe.workbench.base.diagramRendering");
		for (IConfigurationElement configElement : configElements) {
			try {
				IDiagramRenderer renderer = (IDiagramRenderer) configElement
						.createExecutableExtension("renderClass");
				session.registerRenderer(renderer);
			} catch (CoreException e) {
				TigerstripeRuntime.logErrorMessage("CoreException detected", e);
			}
		}

	}

	/**
	 * A listener that gets notified when files change in the Workspace so it
	 * can propagate as appropriate to the artifact manager
	 * 
	 */
	private void startWorkspaceListener() {
		listener = new WorkspaceListener();
		JavaCore.addElementChangedListener(listener);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(listener);
	}

	private void stopWorkspaceListener() {
		if (listener != null) {
			JavaCore.removeElementChangedListener(listener);
			ResourcesPlugin.getWorkspace().removeResourceChangeListener(
					listener);
		}
	}

	private void executePostInstallationActions(BundleContext context)
			throws TigerstripeException {
		(new PostInstallActions()).run(context);
	}

	public static void internalLogMessage(Level level, String message,
			Throwable t) {
		TigerstripeRuntime.logMessage(level, message, t);
	}

	// duplicates of logXXXMessage methods from TigerstripRuntime class
	// that allow for reporting of proper locations when the underlying
	// log4j "log" message is called
	private static void internalLogErrorMessage(String message) {
		internalLogErrorMessage(message, null);
	}

	private static void internalLogErrorMessage(String message, Throwable t) {
		BasePlugin.internalLogMessage(Level.ERROR, message, t);
	}

	private static void internalLogInfoMessage(String message) {
		internalLogInfoMessage(message, null);
	}

	private static void internalLogInfoMessage(String message, Throwable t) {
		BasePlugin.internalLogMessage(Level.INFO, message, t);
	}

	private static void internalLogWarnMessage(String message) {
		internalLogWarnMessage(message, null);
	}

	private static void internalLogWarnMessage(String message, Throwable t) {
		BasePlugin.internalLogMessage(Level.WARN, message, t);
	}

	public static void log(IStatus status) {
		// add the status message to the "Problems" view
		getDefault().getLog().log(status);
		// and then add a message to the Tigerstripe logfile containing the same
		// information
		// that appears in the "Problems" view
		if (status.getSeverity() == IStatus.ERROR) {
			if (status.getException() != null)
				internalLogErrorMessage(status.getMessage(), status
						.getException());
			else
				internalLogErrorMessage(status.getMessage());
		} else if (status.getSeverity() == IStatus.WARNING) {
			if (status.getException() != null)
				internalLogWarnMessage(status.getMessage(), status
						.getException());
			else
				internalLogWarnMessage(status.getMessage());
		} else if (status.getSeverity() == IStatus.INFO) {
			if (status.getException() != null)
				internalLogInfoMessage(status.getMessage(), status
						.getException());
			else
				internalLogInfoMessage(status.getMessage());
		}
	}

	public static void logErrorMessage(String message) {
		// calls through to the static EclipsePlugin.log(IStatus):void method
		// (above)
		// passing this message as an "internal error" status message
		log(new Status(IStatus.ERROR, getPluginId(), 222, message, null));
	}

	public static void logErrorStatus(String message, IStatus status) {
		if (status == null) {
			// log the message as an error message (will add the error to the
			// list
			// of errors maintained in the "Problems" view and the Tigerstripe
			// logfile)
			logErrorMessage(message);
			return;
		}
		MultiStatus multi = new MultiStatus(getPluginId(), 222, message, null);
		// log the status message
		multi.add(status);
		log(multi);
	}

	public static void log(Throwable e) {

		if (e instanceof TigerstripeException) {
			TigerstripeException tse = (TigerstripeException) e;
			if (tse.getException() == null) {
				IStatus status = new Status(IStatus.ERROR, getPluginId(), 222,
						"Internal Error", tse); //$NON-NLS-1$
				log(status);
				return;
			} else {
				MultiStatus mStatus = new MultiStatus(getPluginId(), 222,
						"Internal Error", e);
				Exception ee = tse.getException();

				while (ee != null) {
					IStatus subStatus = new Status(IStatus.ERROR,
							getPluginId(), 222, "Internal Error", ee); //$NON-NLS-1$
					mStatus.add(subStatus);
					if (ee instanceof TigerstripeException) {
						ee = ((TigerstripeException) ee).getException();
					} else if (e.getCause() instanceof Exception) {
						ee = (Exception) ee.getCause();
					} else {
						break;
					}
				}
				log(mStatus);
				return;
			}
		} else {
			if (e.getCause() == null) {
				IStatus status = new Status(IStatus.ERROR, getPluginId(), 222,
						"Internal Error", e); //$NON-NLS-1$
				log(status);
				return;
			} else {
				MultiStatus mStatus = new MultiStatus(getPluginId(), 222,
						"Internal Error", e);
				Throwable ee = e.getCause();

				while (ee != null) {
					IStatus subStatus = new Status(IStatus.ERROR,
							getPluginId(), 222, "Internal Error", ee); //$NON-NLS-1$
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
	}

}
