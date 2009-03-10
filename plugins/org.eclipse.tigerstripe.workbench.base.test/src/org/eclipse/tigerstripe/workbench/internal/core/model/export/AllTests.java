package org.eclipse.tigerstripe.workbench.internal.core.model.export;


import org.eclipse.tigerstripe.workbench.internal.core.model.export.facets.TestFacetExporter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Tests for org.eclipse.tigerstripe.workbench.internal.core.model.export.*");
		//$JUnit-BEGIN$
		suite.addTestSuite(TestExportDiff.class);
		suite.addTestSuite(TestExportFacetManager.class);
		suite.addTestSuite(TestFacetExporter.class);
		//$JUnit-END$
		return suite;
	}
}
