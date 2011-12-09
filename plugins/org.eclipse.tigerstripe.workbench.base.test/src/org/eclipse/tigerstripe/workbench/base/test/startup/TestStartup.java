/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - erdillon
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.base.test.startup;

import java.io.File;
import java.io.FileReader;
import java.nio.CharBuffer;

import junit.framework.TestCase;

import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.startup.PostInstallActions;

/**
 * Upon startup Tigerstripe will create a "tigerstripe" directory within the
 * Eclipse install so it can store its "things".
 * 
 * Check that this is working properly.
 * 
 * @author erdillon
 * 
 */
public class TestStartup extends TestCase {

	public void testStartup() {
		// Force the Tigerstripe Base plugin to activate
		PostInstallActions.init();
		TigerstripeCore.getRuntimeDetails();

		org.eclipse.osgi.service.datalocation.Location installLocation = Platform
				.getInstallLocation();

		// Check that the Tigerstripe directory exists
		File tigerstripeDir = new File(installLocation.getURL().getPath()
				+ File.separator + "tigerstripe");
		assertTrue(tigerstripeDir.exists());

		// Check for the Phantom Dir
		File phantomDir = new File(tigerstripeDir.getAbsolutePath()
				+ File.separator + "phantom");
		assertTrue(phantomDir.exists());
	}

	public void testLog() throws Exception {
		// Check that the log gets created properly
		// Force the Tigerstripe Base plugin to activate
		TigerstripeCore.getRuntimeDetails();

		org.eclipse.osgi.service.datalocation.Location installLocation = Platform
				.getInstallLocation();

		// Check that the Tigerstripe directory exists
		File tigerstripeDir = new File(installLocation.getURL().getPath()
				+ File.separator + "tigerstripe");
		assertTrue(tigerstripeDir.exists());

		File logFile = new File(tigerstripeDir + File.separator
				+ "tigerstripe.log");
		assertTrue(logFile.exists());

		final String logMessage = "Test log";
		BasePlugin.logErrorMessage(logMessage);

		final FileReader reader = new FileReader(logFile);
		final CharBuffer cb = CharBuffer.allocate(8192);
		final StringBuilder sb = new StringBuilder();
		int read = 0;
		while((read = reader.read(cb)) != -1) {
			sb.append(cb.array(), 0, read);
			cb.rewind();
		}
		
		assertTrue("Log file was not updated", sb.toString().contains(logMessage));
	}	

}
