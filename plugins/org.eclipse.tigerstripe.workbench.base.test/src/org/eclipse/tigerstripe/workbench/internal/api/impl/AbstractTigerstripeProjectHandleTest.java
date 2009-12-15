package org.eclipse.tigerstripe.workbench.internal.api.impl;

import junit.framework.TestCase;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.tigerstripe.workbench.base.test.utils.M0ProjectHelper;
import org.eclipse.tigerstripe.workbench.base.test.utils.ModelProjectHelper;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeM0GeneratorProject;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;

public class AbstractTigerstripeProjectHandleTest extends TestCase {

	private static final String TEST_M0_PROJECT = "TestM0Project";
	private static final String TEST_MODEL_PROJECT = "TestModelProject";
	
	private ITigerstripeModelProject modelProject;
	
	private ITigerstripeM0GeneratorProject m0Project;
	
	protected void setUp() throws Exception {
		
		modelProject = ModelProjectHelper.createEmptyModelProject(TEST_MODEL_PROJECT, null);
		m0Project = M0ProjectHelper.createM0Project(TEST_M0_PROJECT, false);
	}

	protected void tearDown() throws Exception {
		
		if(modelProject != null && modelProject.exists()) {
			modelProject.delete(true, new NullProgressMonitor());
		}
		if(m0Project != null && m0Project.exists()) {
			m0Project.delete(true, new NullProgressMonitor());
		}
	}

	public void testGetNameModel() {
		
		assertEquals(modelProject.getName(), TEST_MODEL_PROJECT);
		assertEquals(m0Project.getName(), TEST_M0_PROJECT);
		
	}
	
}
