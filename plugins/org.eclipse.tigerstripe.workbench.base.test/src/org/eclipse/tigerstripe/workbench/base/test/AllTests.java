/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial Version
 *******************************************************************************/
package org.eclipse.tigerstripe.workbench.base.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.tigerstripe.workbench.base.test.model.TestArtifacts;
import org.eclipse.tigerstripe.workbench.base.test.model.TestFields;
import org.eclipse.tigerstripe.workbench.base.test.model.TestLiterals;
import org.eclipse.tigerstripe.workbench.base.test.model.TestMethods;
import org.eclipse.tigerstripe.workbench.base.test.model.TestModelManager;
import org.eclipse.tigerstripe.workbench.base.test.project.TestProjectManagement;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.eclipse.tigerstripe.workbench.base.test");
		// $JUnit-BEGIN$
		suite.addTestSuite(TestTigerstripeCore.class);

		// Basic project operations
		suite.addTestSuite(TestProjectManagement.class);

		// Model operations
	//	suite.addTestSuite(TestModelManager.class); // None of these are implemented yet!
		suite.addTestSuite(TestArtifacts.class);
		suite.addTestSuite(TestFields.class);
		suite.addTestSuite(TestLiterals.class);
		suite.addTestSuite(TestMethods.class);

		// $JUnit-END$
		return suite;
	}

}
