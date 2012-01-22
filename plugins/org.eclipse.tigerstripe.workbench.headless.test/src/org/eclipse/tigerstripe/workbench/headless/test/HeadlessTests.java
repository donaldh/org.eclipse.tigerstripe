package org.eclipse.tigerstripe.workbench.headless.test;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.osgi.framework.Bundle;

/**
 * To run in eclipse, select 'no application[HEADLESS MODE]' in run/debug
 * configuration
 */
@RunWith(Suite.class)
@SuiteClasses({ 
	HeadlessStartupTest.class, 
	HeadlessGenerationTest.class 
})
public class HeadlessTests {

	private static TestApplication application;

	@BeforeClass
	public static void setUp() throws Exception {
		application = new TestApplication();
		application.start(new Context());
	}

	@AfterClass
	public static void tearDown() throws Exception {
		application.stop();
	}

	@SuppressWarnings("rawtypes")
	static class Context implements IApplicationContext {

		private final Map argument = new HashMap();

		@Override
		public Map getArguments() {
			return argument;
		}

		@Override
		public void applicationRunning() {
		}

		@Override
		public String getBrandingApplication() {
			return null;
		}

		@Override
		public String getBrandingName() {
			return null;
		}

		@Override
		public String getBrandingDescription() {
			return null;
		}

		@Override
		public String getBrandingId() {
			return null;
		}

		@Override
		public String getBrandingProperty(String key) {
			return null;
		}

		@Override
		public Bundle getBrandingBundle() {
			return null;
		}

		@Override
		public void setResult(Object result, IApplication application) {
		}
	}
}
