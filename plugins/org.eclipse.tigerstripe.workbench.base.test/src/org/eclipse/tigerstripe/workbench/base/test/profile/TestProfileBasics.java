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
package org.eclipse.tigerstripe.workbench.base.test.profile;

import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.internal.core.profile.WorkbenchProfile;
import org.eclipse.tigerstripe.workbench.internal.core.profile.primitiveType.PrimitiveTypeDef;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfile;
import org.eclipse.tigerstripe.workbench.profile.IWorkbenchProfileSession;
import org.eclipse.tigerstripe.workbench.profile.primitiveType.IPrimitiveTypeDef;

import junit.framework.TestCase;

public class TestProfileBasics extends TestCase {

	public void testDefaultProfile() throws Exception {
		IWorkbenchProfileSession session = TigerstripeCore
				.getWorkbenchProfileSession();
		assertNotNull(session);
		IWorkbenchProfile profile = session.getActiveProfile();
		assertNotNull(profile);
		
		assertNotNull(profile.getStereotypes());
		assertTrue( profile.getStereotypes().size() == 0);
		
		IPrimitiveTypeDef defaultPType = profile.getDefaultPrimitiveType();
		assertNotNull(defaultPType);
	}

	public void createSimpleProfile() throws Exception {
		IWorkbenchProfileSession session = TigerstripeCore.getWorkbenchProfileSession();
		
		IWorkbenchProfile profile = session.makeWorkbenchProfile();
		
		WorkbenchProfile p = (WorkbenchProfile) profile;
		p.setName("testProfile");
		PrimitiveTypeDef def = new PrimitiveTypeDef();
		def.setName("foo");
		def.setPackageName("primitive");
		p.addPrimitiveTypeDef(def);
		
		session.saveAsActiveProfile(profile);
		
		IWorkbenchProfile active = session.getActiveProfile();
		assertTrue( "testProfile".equals(active.getName()));
		
	}
}
