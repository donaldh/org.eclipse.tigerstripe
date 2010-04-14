/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial version
 *******************************************************************************/

package org.eclipse.tigerstripe.mojo;

import junit.framework.TestCase;

public class ModuleGenerationMojoTest extends TestCase {

	public void testGetExecutableForOs() {

		ModuleGenerationMojo mojo = new ModuleGenerationMojo();

		try {
			mojo.getExecutableForOs("unsupported os");
			fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException uoe) {
			// pass
		}

		assertEquals(ModuleGenerationMojo.MAC_ECLIPSE_EXE, mojo
				.getExecutableForOs("Mac OS"));
		assertEquals(ModuleGenerationMojo.WIN_ECLIPSE_EXE, mojo
				.getExecutableForOs("Windows XP"));
		assertEquals(ModuleGenerationMojo.WIN_ECLIPSE_EXE, mojo
				.getExecutableForOs("Windows 2003"));
		assertEquals(ModuleGenerationMojo.LNX_ECLIPSE_EXE, mojo
				.getExecutableForOs("Linux"));

		// Test based on build environment (Linux is excluded as I don't have a
		// Linux box to test on)
		if (System.getProperty("os.name").startsWith("Mac OS")) {
			assertEquals(ModuleGenerationMojo.MAC_ECLIPSE_EXE, mojo
					.getExecutableForOs(System.getProperty("os.name")));
		} else if (System.getProperty("os.name").startsWith("Windows")) {
			assertEquals(ModuleGenerationMojo.WIN_ECLIPSE_EXE, mojo
					.getExecutableForOs(System.getProperty("os.name")));
		}

	}

}
