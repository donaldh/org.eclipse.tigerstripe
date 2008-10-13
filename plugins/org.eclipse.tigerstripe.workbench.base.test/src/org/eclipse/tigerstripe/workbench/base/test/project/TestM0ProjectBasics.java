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
package org.eclipse.tigerstripe.workbench.base.test.project;

import junit.framework.TestCase;

import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.WorkingCopyException;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM0GeneratorProject;

public class TestM0ProjectBasics extends TestCase {

	private ITigerstripeM0GeneratorProject project;

	@Override
	protected void setUp() throws Exception {
		IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();
		project = (ITigerstripeM0GeneratorProject) TigerstripeCore
				.createProject("TestM0ProjectBasics", projectDetails, null,
						ITigerstripeM0GeneratorProject.class, null, null);
	}

	@Override
	protected void tearDown() throws Exception {
		if (project != null && project.exists())
			project.delete(true, null);
	}

	public <T extends IPluginProperty> void testSupportedPluginProperties()
			throws TigerstripeException {
		Class<T> supported[] = project.getSupportedProperties();

		assertTrue(supported.length != 0);

		for (Class<T> propertyType : supported) {
			IPluginProperty property = project.makeProperty(propertyType);
			assertNotNull(property);

			// First adding to original should fail
			try {
				property.setName("property");
				project.addGlobalProperty(property);
				fail("addGlobalProperty on original shouldn't be allowed.");
			} catch (WorkingCopyException e) {
				ITigerstripeM0GeneratorProject copy = (ITigerstripeM0GeneratorProject) project
						.makeWorkingCopy(null);
				copy.addGlobalProperty(property);
			}

			// First removing to original should fail
			try {
				property.setName("property");
				project.removeGlobalProperty(property);
				fail("addGlobalProperty on original shouldn't be allowed.");
			} catch (WorkingCopyException e) {
				ITigerstripeM0GeneratorProject copy = (ITigerstripeM0GeneratorProject) project
						.makeWorkingCopy(null);
				copy.removeGlobalProperty(property);
			}
		}
	}

	public <T extends IRule> void testSupportedGlobalRules()
			throws TigerstripeException {
		Class<T> supported[] = project.getSupportedGlobalRules();

		assertTrue(supported.length != 0);

		for (Class<T> propertyType : supported) {
			IRule property = project.makeRule(propertyType);
			assertNotNull(property);
		}
	}
}
