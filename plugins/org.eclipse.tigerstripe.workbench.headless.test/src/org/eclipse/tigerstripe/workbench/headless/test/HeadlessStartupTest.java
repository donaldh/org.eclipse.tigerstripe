package org.eclipse.tigerstripe.workbench.headless.test;

import junit.framework.TestCase;

import org.eclipse.tigerstripe.workbench.internal.core.TigerstripeRuntime;

public class HeadlessStartupTest extends TestCase {

	public void testInitialization() throws Exception {
		assertNotNull(TigerstripeRuntime.getTigerstripeRuntimeRoot());
		try {
			TigerstripeRuntime.logInfoMessage("TEST");
		} catch (NullPointerException e) {
			fail("Logger isn't initialized");
		}
	}
}
