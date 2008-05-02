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

import org.eclipse.tigerstripe.workbench.base.test.builders.TestBasicM1ProjectAuditor;
import org.eclipse.tigerstripe.workbench.base.test.builders.TestBasicModelProjectAuditor;
import org.eclipse.tigerstripe.workbench.base.test.facet.BasicFacetCreationTest;
import org.eclipse.tigerstripe.workbench.base.test.generation.TestM1Generation;
import org.eclipse.tigerstripe.workbench.base.test.generation.TestProjectGenerationBasics;
import org.eclipse.tigerstripe.workbench.base.test.migration.TestArtifactRefactor;
import org.eclipse.tigerstripe.workbench.base.test.migration.TestFieldMigration;
import org.eclipse.tigerstripe.workbench.base.test.migration.TestManagedEntityMigration;
import org.eclipse.tigerstripe.workbench.base.test.migration.TestModelManager;
import org.eclipse.tigerstripe.workbench.base.test.model.TestArtifacts;
import org.eclipse.tigerstripe.workbench.base.test.model.TestFields;
import org.eclipse.tigerstripe.workbench.base.test.model.TestLiterals;
import org.eclipse.tigerstripe.workbench.base.test.model.TestMethods;
import org.eclipse.tigerstripe.workbench.base.test.model.TestTigerstripeWorkspaceNotifications;
import org.eclipse.tigerstripe.workbench.base.test.project.TestM1ProjectContents;
import org.eclipse.tigerstripe.workbench.base.test.project.TestModelProjectLifecycle;
import org.eclipse.tigerstripe.workbench.base.test.project.TestM1ProjectBasics;
import org.eclipse.tigerstripe.workbench.base.test.project.TestProjectManagement;
import org.eclipse.tigerstripe.workbench.base.test.startup.TestStartup;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.eclipse.tigerstripe.workbench.base.test");
		// $JUnit-BEGIN$

		// Startup Tests
		suite.addTestSuite(TestStartup.class);

		suite.addTestSuite(TestTigerstripeCore.class);

		// Basic project operations
		suite.addTestSuite(TestProjectManagement.class);
		suite.addTestSuite(TestModelProjectLifecycle.class);
		suite.addTestSuite(TestM1ProjectBasics.class);
		suite.addTestSuite(TestM1ProjectContents.class);

		// Builder tests
		suite.addTestSuite(TestBasicM1ProjectAuditor.class);
		suite.addTestSuite(TestBasicModelProjectAuditor.class);

		// Facet Tests
		suite.addTestSuite(BasicFacetCreationTest.class);

		// Metamodel migration test
		suite.addTestSuite(TestArtifactRefactor.class);
		suite.addTestSuite(TestFieldMigration.class);
		suite.addTestSuite(TestManagedEntityMigration.class);
		suite.addTestSuite(TestModelManager.class);

		// Generation Tests
		suite.addTestSuite(TestProjectGenerationBasics.class);
		suite.addTestSuite(TestM1Generation.class);

		// Model operations
		// suite.addTestSuite(TestModelManager.class); // None of these are
		// implemented yet!
		suite.addTestSuite(TestArtifacts.class);
		suite.addTestSuite(TestFields.class);
		suite.addTestSuite(TestLiterals.class);
		suite.addTestSuite(TestMethods.class);
		suite.addTestSuite(TestTigerstripeWorkspaceNotifications.class);

		// $JUnit-END$
		return suite;
	}
}
