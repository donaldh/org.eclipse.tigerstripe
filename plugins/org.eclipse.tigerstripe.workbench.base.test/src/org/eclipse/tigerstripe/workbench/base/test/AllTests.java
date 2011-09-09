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

import org.eclipse.tigerstripe.workbench.base.test.adapt.TestAdapters;
import org.eclipse.tigerstripe.workbench.base.test.adapt.TestTigerstripeURIAdapterFactory;
import org.eclipse.tigerstripe.workbench.base.test.annotation.TestAnnotationCreationAPI;
import org.eclipse.tigerstripe.workbench.base.test.annotation.TestChangeIdRefactoringListener;
import org.eclipse.tigerstripe.workbench.base.test.annotation.TestTigerstripeAnnotationResourceProcessor;
import org.eclipse.tigerstripe.workbench.base.test.builders.TestBasicM1ProjectAuditor;
import org.eclipse.tigerstripe.workbench.base.test.builders.TestBasicModelProjectAuditor;
import org.eclipse.tigerstripe.workbench.base.test.core.model.TestContextProjectAwareArtifact;
import org.eclipse.tigerstripe.workbench.base.test.core.project.pluggable.rules.TestRule;
import org.eclipse.tigerstripe.workbench.base.test.facet.BasicFacetTest;
import org.eclipse.tigerstripe.workbench.base.test.facet.TestFacetResolution;
import org.eclipse.tigerstripe.workbench.base.test.generation.TestGenerateCompleteFramework;
import org.eclipse.tigerstripe.workbench.base.test.generation.TestM0DeployUndeploy;
import org.eclipse.tigerstripe.workbench.base.test.generation.TestM1Generation;
import org.eclipse.tigerstripe.workbench.base.test.generation.TestProjectGenerationBasics;
import org.eclipse.tigerstripe.workbench.base.test.model.TestArtifacts;
import org.eclipse.tigerstripe.workbench.base.test.model.TestFields;
import org.eclipse.tigerstripe.workbench.base.test.model.TestLiterals;
import org.eclipse.tigerstripe.workbench.base.test.model.TestMethods;
import org.eclipse.tigerstripe.workbench.base.test.model.TestTigerstripeWorkspaceNotifications;
import org.eclipse.tigerstripe.workbench.base.test.patterns.PatternTest;
import org.eclipse.tigerstripe.workbench.base.test.patterns.ProjectPatternTest;
import org.eclipse.tigerstripe.workbench.base.test.profile.TestProfileBasics;
import org.eclipse.tigerstripe.workbench.base.test.project.TestM0ProjectBasics;
import org.eclipse.tigerstripe.workbench.base.test.project.TestM0ProjectContents;
import org.eclipse.tigerstripe.workbench.base.test.project.TestM1ProjectBasics;
import org.eclipse.tigerstripe.workbench.base.test.project.TestM1ProjectContents;
import org.eclipse.tigerstripe.workbench.base.test.project.TestModelProjectLifecycle;
import org.eclipse.tigerstripe.workbench.base.test.project.TestProjectManagement;
import org.eclipse.tigerstripe.workbench.base.test.references.TestModelReferences;
import org.eclipse.tigerstripe.workbench.base.test.references.TestProjectReferences1;
import org.eclipse.tigerstripe.workbench.base.test.startup.TestStartup;
import org.eclipse.tigerstripe.workbench.internal.core.model.export.TestExportDiff;
import org.eclipse.tigerstripe.workbench.internal.core.model.export.TestExportFacetManager;
import org.eclipse.tigerstripe.workbench.internal.core.model.export.facets.TestFacetExporter;

public class AllTests {

	public static Test suite() {

		TestSuite suite = new TestSuite("org.eclipse.tigerstripe.workbench.base.test");

		// Startup Tests
		suite.addTestSuite(TestStartup.class);
		suite.addTestSuite(TestTigerstripeCore.class);

		// Basic project operations
		suite.addTestSuite(TestProjectManagement.class);
		suite.addTestSuite(TestModelProjectLifecycle.class);
		suite.addTestSuite(TestM1ProjectBasics.class);
		suite.addTestSuite(TestM1ProjectContents.class);
		suite.addTestSuite(TestM0ProjectBasics.class);
		suite.addTestSuite(TestM0ProjectContents.class);

		// Adapters
		suite.addTestSuite(TestAdapters.class);
		suite.addTestSuite(TestTigerstripeURIAdapterFactory.class);
		
		// Builder tests
		suite.addTestSuite(TestBasicM1ProjectAuditor.class);
		suite.addTestSuite(TestBasicModelProjectAuditor.class);

		// Facet Tests
		suite.addTestSuite(BasicFacetTest.class);
		suite.addTestSuite(TestFacetResolution.class);

		// Generation Tests
		suite.addTestSuite(TestGenerateCompleteFramework.class);
		suite.addTestSuite(TestProjectGenerationBasics.class);
		suite.addTestSuite(TestM1Generation.class);
		suite.addTestSuite(TestM0DeployUndeploy.class);
		suite.addTestSuite(TestRule.class);

		// Model operations
		suite.addTestSuite(TestArtifacts.class);
		suite.addTestSuite(TestFields.class);
		suite.addTestSuite(TestLiterals.class);
		suite.addTestSuite(TestMethods.class);
		suite.addTestSuite(TestTigerstripeWorkspaceNotifications.class);
		suite.addTestSuite(TestContextProjectAwareArtifact.class);
		
		// Profiles
		suite.addTestSuite(TestProfileBasics.class);

		// Annotations
		suite.addTestSuite(TestAnnotationCreationAPI.class);
		suite.addTestSuite(TestTigerstripeAnnotationResourceProcessor.class);
		suite.addTestSuite(TestChangeIdRefactoringListener.class);

		// Patterns
		suite.addTestSuite(PatternTest.class);
		suite.addTestSuite(ProjectPatternTest.class);

		// References
		suite.addTestSuite(TestProjectReferences1.class);
		suite.addTestSuite(TestModelReferences.class);

		// Export
		suite.addTestSuite(TestExportDiff.class);
		suite.addTestSuite(TestExportFacetManager.class);
		suite.addTestSuite(TestFacetExporter.class);

		return suite;
	}
}
