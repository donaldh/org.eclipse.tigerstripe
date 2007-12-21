/*******************************************************************************
 * Copyright (c) 2007 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    E. Dillon (Cisco Systems, Inc.) - reformat for Code Open-Sourcing
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.ui.eclipse.actions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.tigerstripe.eclipse.EclipsePlugin;
import org.eclipse.tigerstripe.workbench.ui.eclipse.TigerstripePluginConstants;

/**
 * Opens a URL in the default browser
 * 
 * @author Eric Dillon
 * 
 */
public class OpenBrowserAction extends Action {

	private URL url;

	public URL getURL() {
		return this.url;
	}

	public void setURL(URL url) {
		this.url = url;
	}

	static boolean mailtoFailed = false;

	public static URL newURL(String href) {
		URL newURL = null;
		try {
			newURL = new URL(href);
		} catch (MalformedURLException e) {
			if (href.startsWith("mailto:")) {
				if (!mailtoFailed) {
					mailtoFailed = true;
					Status status = new Status(IStatus.ERROR,
							TigerstripePluginConstants.PLUGIN_ID, 222,
							"mailto protocol not supported", e);
					EclipsePlugin.log(status);
				}
			} else {
				Status status = new Status(IStatus.ERROR,
						TigerstripePluginConstants.PLUGIN_ID, 222,
						"Invalid URL spec: " + href, e);
				EclipsePlugin.log(status);
			}
		}
		return newURL;
	}

	static boolean isBrowserOpen;

	@Override
	public void run() {
		browse(getURL());
	}

	public static void browse(URL url) {
		if (url != null)
			browse(url.toExternalForm());
	}

	public static void browse(final String href) {
		if (href == null)
			return;

		// Windows
		if (SWT.getPlatform().equals("win32")) {
			Program.launch(href);
			return;
		}

		// Non-windows
		Thread launcher = new Thread("Browser Launcher") {
			@Override
			public void run() {
				try {
					if (isBrowserOpen) {
						Runtime.getRuntime().exec(
								"netscape -remote openURL(" + href + ")");
					} else {
						Process p = Runtime.getRuntime().exec(
								"netscape " + href);
						isBrowserOpen = true;
						try {
							if (p != null)
								p.waitFor();
						} catch (InterruptedException e) {
							asyncShowError("Failed to open browser on " + href,
									e);
						} finally {
							isBrowserOpen = false;
						}
					}
				} catch (IOException e) {
					asyncShowError("Unable to open browser on " + href, e);
				}
			}
		};
		launcher.start();
	}

	public static void asyncShowError(final String msg, final Throwable ex) {
		EclipsePlugin.log(ex);
		final String title = EclipsePlugin.getDefault().getDescriptor()
				.getLabel();
		final Display display = Display.getDefault();
		display.asyncExec(new Runnable() {
			public void run() {
				Shell shell = display.getActiveShell();
				MessageDialog dialog = new MessageDialog(shell, title, null,
						msg, MessageDialog.ERROR, new String[] { "Ok" }, 0);
				dialog.open();
			}
		});
	}

	public OpenBrowserAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OpenBrowserAction(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public OpenBrowserAction(String text, ImageDescriptor image) {
		super(text, image);
		// TODO Auto-generated constructor stub
	}

	public OpenBrowserAction(String text, int style) {
		super(text, style);
		// TODO Auto-generated constructor stub
	}

}
