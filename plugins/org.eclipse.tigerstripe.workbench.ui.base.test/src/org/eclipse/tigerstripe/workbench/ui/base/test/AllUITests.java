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
package org.eclipse.tigerstripe.workbench.ui.base.test;

import org.eclipse.tigerstripe.workbench.ui.base.test.wizards.artifacts.TestNewManagedEntityWizardOperations;
import org.eclipse.tigerstripe.workbench.ui.base.test.wizards.generator.TestNewGeneratorProjectWizardOperations;
import org.eclipse.tigerstripe.workbench.ui.base.test.wizards.project.TestNewProjectWizardOperations;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllUITests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.eclipse.tigerstripe.workbench.ui.base.test");
		// $JUnit-BEGIN$
		suite.addTestSuite(TestNewProjectWizardOperations.class);
		
		suite.addTestSuite(TestNewManagedEntityWizardOperations.class);
		
		suite.addTestSuite(TestNewGeneratorProjectWizardOperations.class);
		
		return suite;
	}
}
