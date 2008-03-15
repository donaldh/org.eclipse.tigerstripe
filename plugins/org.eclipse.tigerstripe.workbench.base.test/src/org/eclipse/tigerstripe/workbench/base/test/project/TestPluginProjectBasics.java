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
import org.eclipse.tigerstripe.workbench.plugins.EPluggablePluginNature;
import org.eclipse.tigerstripe.workbench.plugins.IArtifactBasedTemplateRunRule;
import org.eclipse.tigerstripe.workbench.plugins.IPluginProperty;
import org.eclipse.tigerstripe.workbench.plugins.IRunRule;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripePluginProject;

public class TestPluginProjectBasics extends TestCase {

	private ITigerstripePluginProject project;

	@Override
	protected void setUp() throws Exception {
		IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();
		projectDetails.setName("TestPluginProjectBasics");
		project = (ITigerstripePluginProject) TigerstripeCore.createProject(
				projectDetails, null, ITigerstripePluginProject.class, null,
				null);
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
				ITigerstripePluginProject copy = (ITigerstripePluginProject) project
						.makeWorkingCopy(null);
				copy.addGlobalProperty(property);
			}

			// First removing to original should fail
			try {
				property.setName("property");
				project.removeGlobalProperty(property);
				fail("addGlobalProperty on original shouldn't be allowed.");
			} catch (WorkingCopyException e) {
				ITigerstripePluginProject copy = (ITigerstripePluginProject) project
						.makeWorkingCopy(null);
				copy.removeGlobalProperty(property);
			}
		}
	}

	public <T extends IRunRule> void testSupportedGlobalRules()
			throws TigerstripeException {
		Class<T> supported[] = project.getSupportedPluginRules();

		assertTrue(supported.length != 0);

		for (Class<T> propertyType : supported) {
			IRunRule property = project.makeRule(propertyType);
			assertNotNull(property);
		}
	}

	public <T extends IArtifactBasedTemplateRunRule> void testSupportedArtifactBasedRules()
			throws TigerstripeException {
		Class<T> supported[] = project.getSupportedPluginArtifactRules();

		assertTrue(supported.length != 0);

		for (Class<T> propertyType : supported) {
			IRunRule property = project.makeRule(propertyType);
			assertNotNull(property);
		}
	}

	public void testSetPluginNature() throws TigerstripeException {
		try {
			project.setPluginNature(EPluggablePluginNature.Generic);
			fail("setPluginNature should fail on original.");
		} catch (WorkingCopyException e) {
			ITigerstripePluginProject copy = (ITigerstripePluginProject) project
					.makeWorkingCopy(null);
			copy.setPluginNature(EPluggablePluginNature.Generic);
			copy.commit(null);
		}

		EPluggablePluginNature nature = project.getPluginNature();
		assertTrue(nature == EPluggablePluginNature.Generic);
	}
}
