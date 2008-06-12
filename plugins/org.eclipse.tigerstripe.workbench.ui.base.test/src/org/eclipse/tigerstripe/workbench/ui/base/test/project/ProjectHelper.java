package org.eclipse.tigerstripe.workbench.ui.base.test.project;

import java.util.ArrayList;

import org.eclipse.tigerstripe.workbench.ui.base.test.suite.TestingConstants;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class ProjectHelper  extends UITestCaseSWT {

	
	public static void checkArtifactsInExplorer (IUIContext ui, ArrayList<String> artifacts ){
		for (String artifact : artifacts){

			String pathToItem = TestingConstants.NEW_PROJECT_NAME+
			"/src/"+
			TestingConstants.DEFAULT_ARTIFACT_PACKAGE+"/"+
			artifact;

			try {	
				TreeItemLocator treeItem = new TreeItemLocator(
						pathToItem,
					new ViewLocator(
							"org.eclipse.tigerstripe.workbench.views.artifactExplorerViewNew"));
				ui.click(treeItem);
			} catch (Exception e){
				fail("Artifact '"+ pathToItem+ "' is not in the Explorer view");
			}
		}
		
	}
	
}
