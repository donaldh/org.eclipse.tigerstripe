package org.eclipse.tigerstripe.workbench.internal.core.model.export.facets;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.eclipse.tigerstripe.workbench.internal.core.model.export.facets");
		//$JUnit-BEGIN$
		suite.addTestSuite(FacetModelExporterFacetManagerTest.class);
		//$JUnit-END$
		return suite;
	}

}
