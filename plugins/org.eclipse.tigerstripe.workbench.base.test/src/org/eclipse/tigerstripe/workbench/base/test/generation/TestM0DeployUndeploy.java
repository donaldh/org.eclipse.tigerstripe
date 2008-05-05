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
package org.eclipse.tigerstripe.workbench.base.test.generation;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.runtime.IPath;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.base.test.utils.M0ProjectHelper;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginManager;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggableHousing;
import org.eclipse.tigerstripe.workbench.project.GeneratorDeploymentHelper;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM0GeneratorProject;

public class TestM0DeployUndeploy extends TestCase {

	private ITigerstripeM0GeneratorProject generator;

	private static final String GENERATOR = "TestM0DeployUndeploy";

	@Override
	protected void setUp() throws Exception {
		generator = M0ProjectHelper.createM0Project(GENERATOR, true);
	}

	@Override
	protected void tearDown() throws Exception {
		if (generator != null && generator.exists())
			generator.delete(true, null);
	}

	public void testDeployUnDeploy() throws TigerstripeException {
		GeneratorDeploymentHelper helper = new GeneratorDeploymentHelper();

		assertTrue(helper.canDeploy(generator));
		IPath path = helper.deploy(generator, null);

		// Check that the plugin is now register
		boolean housingFound = false;
		List<PluggableHousing> housings = PluginManager.getManager()
				.getRegisteredPluggableHousings();
		for (PluggableHousing housing : housings) {
			String id = housing.getPluginId();
			if (id.equals(generator.getId())) {
				housingFound = true;
				break;
			}
		}
		assertTrue(housingFound);

		helper.unDeploy(path, null);

		// Check that the plugin is now register
		housingFound = false;
		housings = PluginManager.getManager().getRegisteredPluggableHousings();
		for (PluggableHousing housing : housings) {
			String id = housing.getPluginId();
			if (id.equals(generator.getId())) {
				housingFound = true;
				break;
			}
		}
		assertFalse(housingFound);
	}

}
