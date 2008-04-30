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

import junit.framework.TestCase;

import org.eclipse.core.runtime.Platform;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;

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

		// Check for bin dir and its content
		File binDir = new File(tigerstripeDir.getAbsolutePath()
				+ File.separator + "bin");
		assertTrue(phantomDir.exists());
		assertTrue(new File(binDir.getAbsolutePath() + File.separator
				+ "tigerstripe").exists());
		assertTrue(new File(binDir.getAbsolutePath() + File.separator
				+ "tigerstripe.bat").exists());
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
		long tstamp = logFile.lastModified();

		BasePlugin.logErrorMessage("Test log");
		Thread.sleep(2000); // wait to give log4j some time to flush
		long tstamp2 = logFile.lastModified();
		assertTrue(tstamp < tstamp2);
	}

}
