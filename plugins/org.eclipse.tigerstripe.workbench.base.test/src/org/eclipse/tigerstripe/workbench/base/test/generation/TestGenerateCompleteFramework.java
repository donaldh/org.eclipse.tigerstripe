/******************************************************************************
 * Copyright (c) 2009 by Cisco Systems, Inc. All rights reserved.
 * 
 * This software contains proprietary information which shall not be reproduced
 * or transferred to other documents and shall not be disclosed to others or
 * used for manufacturing or any other purpose without prior permission of Cisco
 * Systems.
 * 
 * Contributors:
 *    Cisco Systems, Inc. - danijoh2
 *****************************************************************************/
package org.eclipse.tigerstripe.workbench.base.test.generation;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.TigerstripeCore;
import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.base.test.utils.M1ProjectHelper;
import org.eclipse.tigerstripe.workbench.generation.IM1RunConfig;
import org.eclipse.tigerstripe.workbench.generation.PluginRunStatus;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.api.impl.TigerstripeProjectHandle;
import org.eclipse.tigerstripe.workbench.internal.core.generation.GenerateCompleteManager;
import org.eclipse.tigerstripe.workbench.internal.core.generation.RunConfig;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.PluginManager;
import org.eclipse.tigerstripe.workbench.internal.core.plugin.pluggable.PluggableHousing;
import org.eclipse.tigerstripe.workbench.project.GeneratorDeploymentHelper;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.IProjectDetails;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM1GeneratorProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class TestGenerateCompleteFramework extends TestCase {
    private ITigerstripeModelProject project;
    private ITigerstripeM1GeneratorProject generator;

    private static final String PROJECT_NAME = "TestGenerateProject";
    private static final String GENERATOR = "TestM1GenerationGenerator";

    @Override
    protected void setUp() throws Exception {
        IProjectDetails projectDetails = TigerstripeCore.makeProjectDetails();
        project = (ITigerstripeModelProject) TigerstripeCore.createProject(
                PROJECT_NAME, projectDetails, null,
                ITigerstripeModelProject.class, null, null);
        generator = M1ProjectHelper.createM1Project(GENERATOR, true);
    }

    @Override
    protected void tearDown() throws Exception {
        if (project != null && project.exists())
            project.delete(true, null);

        if (generator != null && generator.exists())
            generator.delete(true, null);
    }

    public void testGenerateCompleteSuccessListenerIsNotified()
            throws TigerstripeException {
        ITigerstripeModelProject wProj = (ITigerstripeModelProject) project
        .makeWorkingCopy(null);
        GeneratorDeploymentHelper helper = new GeneratorDeploymentHelper();
        
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
        assertTrue("Status length is not 2 : " + status.length,
                status.length == 2);// 1 for plugin and 1 for sample listener
        
        helper.unDeploy(path, null);
    }

    public void testGenerateCompleteSuccessListenerIsNotNotified()
            throws TigerstripeException {
        ITigerstripeModelProject wProj = (ITigerstripeModelProject) project
                .makeWorkingCopy(null);
        GeneratorDeploymentHelper helper = new GeneratorDeploymentHelper();

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
        assertTrue("Status length is not 2 : " + status.length,
                status.length == 2);// 1 for plugin and 1 for sample listener
        
        helper.unDeploy(path, null);
    }
}
