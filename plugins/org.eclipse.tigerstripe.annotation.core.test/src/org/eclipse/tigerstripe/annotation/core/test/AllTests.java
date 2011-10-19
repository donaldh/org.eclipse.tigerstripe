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
package org.eclipse.tigerstripe.annotation.core.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.eclipse.tigerstripe.annotation.core.test");
		// $JUnit-BEGIN$
		suite.addTestSuite(NoTest.class);
		suite.addTestSuite(ExternalResourceTest.class);
		suite.addTestSuite(DelegatesTest.class);
		suite.addTestSuite(AnnotationUtilsTest.class);
		suite.addTestSuite(AddRemoveAnnotationTest.class);
		suite.addTestSuite(ChangeRecordingTest.class);
		suite.addTestSuite(TargetsTest.class);
		suite.addTestSuite(URIPrefixReplacingTest.class);
		suite.addTestSuite(CreateManyAnnotations.class);
		suite.addTestSuite(AnnotationTypeTest.class);
		suite.addTestSuite(AnnotationResourceTest.class);
		suite.addTestSuite(AnnotationFilesFilterTest.class);
		// $JUnit-END$
		return suite;
	}

}
