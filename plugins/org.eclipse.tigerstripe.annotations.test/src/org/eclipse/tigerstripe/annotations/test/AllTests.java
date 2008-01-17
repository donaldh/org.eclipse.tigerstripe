/*******************************************************************************
 * Copyright (c) 2008 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Cisco Systems, Inc. - Initial API and Implementation
 *******************************************************************************/
package org.eclipse.tigerstripe.annotations.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.eclipse.tigerstripe.annotations.test");
		// $JUnit-BEGIN$
		suite.addTestSuite(TestRegistry.class);
		suite.addTestSuite(TestAnnotationScheme.class);
		// $JUnit-END$
		return suite;
	}

}
