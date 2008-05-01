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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.base.test.utils.M1ProjectHelper;
import org.eclipse.tigerstripe.workbench.base.test.utils.ModelProjectHelper;
import org.eclipse.tigerstripe.workbench.generation.IM1RunConfig;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginManager;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggableHousing;
import org.eclipse.tigerstripe.workbench.project.GeneratorDeploymentHelper;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestM1Generation extends TestCase {

	private ITigerstripeModelProject project;
	private ITigerstripeM1GeneratorProject generator;

	private static final String MODEL = "TestM1Generation";
	private static final String GENERATOR = "TestM1GenerationGenerator";

	@Override
	protected void setUp() throws Exception {
		project = ModelProjectHelper.createModelProject(MODEL);
		generator = M1ProjectHelper.createM1Project(GENERATOR, true);
	}

	@Override
	protected void tearDown() throws Exception {
		if (project != null && project.exists())
			project.delete(true, null);

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

	public void testGenerate() throws TigerstripeException {

		ITigerstripeModelProject wProj = (ITigerstripeModelProject) project.makeWorkingCopy(null);
		GeneratorDeploymentHelper helper = new GeneratorDeploymentHelper();

		assertTrue(helper.canDeploy(generator));
		IPath path = helper.deploy(generator, null);

		List<PluggableHousing> housings = PluginManager.getManager()
				.getRegisteredPluggableHousings();

		PluggableHousing housing = housings.get(0);
		IPluginConfig pluginConfig = housing
				.makeDefaultPluginConfig((TigerstripeProjectHandle) project);
		wProj.addPluginConfig(pluginConfig);
		wProj.commit(null);

		IM1RunConfig runConfig = (IM1RunConfig) RunConfig.newGenerationConfig(
				project, RunConfig.M1);
		PluginRunStatus[] status = project.generate(runConfig, null);
		assertTrue( status.length == 1);
		assertTrue( status[0].isOK() );
		
		// Look for generated file
		IProject iProj = (IProject) project.getAdapter(IProject.class);
		IResource res = iProj.findMember("target/tigerstripe.gen/list.txt");
		assertNotNull(res);
		assertTrue(res.exists());

		helper.unDeploy(path, null);
	}
}
