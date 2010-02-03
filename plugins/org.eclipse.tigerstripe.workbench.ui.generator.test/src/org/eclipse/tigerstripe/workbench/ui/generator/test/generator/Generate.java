package org.eclipse.tigerstripe.workbench.ui.generator.test.generator;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;

public class Generate extends UITestCaseSWT {

	/**
	 * Main test method.
	 */
	public void testGenerate() throws Exception {
		IUIContext ui = getUI();
		GenerateHelper helper = new GenerateHelper(ui);
		
//		helper.enablePlugin();
		
		helper.runGenerate();
		helper.checkGlobal();
		helper.checkArtifact();
		helper.checkExtras();
		
	}

}