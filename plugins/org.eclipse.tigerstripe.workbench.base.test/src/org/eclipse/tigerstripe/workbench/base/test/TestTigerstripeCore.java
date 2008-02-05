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

import junit.framework.TestCase;

import org.eclipse.tigerstripe.workbench.IRuntimeDetails;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;

/**
 * Basic test for TigerstripeCore.
 * 
 * Note that Project and Profile related operations are tested separately
 * 
 * @author erdillon
 * 
 */
public class TestTigerstripeCore extends TestCase {

	public void testGetRuntimeDetails() {
		IRuntimeDetails details = TigerstripeCore.getRuntimeDetails();
		assertNotNull(details);

		assertNotNull(details
				.getBaseBundleValue(org.osgi.framework.Constants.BUNDLE_NAME));
	}

}
