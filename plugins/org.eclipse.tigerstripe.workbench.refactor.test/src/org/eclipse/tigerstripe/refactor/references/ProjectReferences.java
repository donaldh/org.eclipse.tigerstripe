package org.eclipse.tigerstripe.refactor.references;

import org.eclipse.tigerstripe.refactor.project.ProjectHelper;
import org.eclipse.tigerstripe.workbench.ui.base.test.project.ArtifactHelper;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;

public class ProjectReferences extends UITestCaseSWT{
	
	private static IUIContext ui;
	private ProjectHelper helper;
	private ArtifactHelper artifactHelper;
	
	
	public void setUp() throws Exception{
		ui = getUI();
		helper = new ProjectHelper();
		artifactHelper = new ArtifactHelper();
		
		helper.loadProjectFromCVS(ui);
		
	}

}
