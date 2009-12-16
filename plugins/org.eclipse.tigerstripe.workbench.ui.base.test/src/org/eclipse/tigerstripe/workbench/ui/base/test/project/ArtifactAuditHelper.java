package org.eclipse.tigerstripe.workbench.ui.base.test.project;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class ArtifactAuditHelper extends UITestCaseSWT {

	IUIContext ui;

	private String errors = "Errors \\([0-9]* item[s]?\\)/";
	private String warnings  = "Warnings \\([0-9]* item[s]?\\)/";
	private String infos  = "Infos \\([0-9]* item[s]?\\)/";
	
	
	public ArtifactAuditHelper(IUIContext ui) {
		super();
		this.ui = ui;
	}
	
	
	public void checkUndefinedReturnType(String artifact, boolean expected) throws Exception{
		checkRule(errors, "Undefined returned Entity type referenced in '"+artifact+"'." ,
				 expected);		
	}
	
	public void checkAssociationEndNeedsNavigation(String artifact, boolean expected) throws Exception{
		checkRule(errors, "At least one Association End must be navigable in '"+artifact+"'." ,
				 expected);		
	}
	
	private void checkRule(
				String type, 
				String message,

				boolean expected){
		TreeItemLocator auditRule = new TreeItemLocator(
				type+message,
				new ViewLocator("org.eclipse.ui.views.ProblemView"));
		try {
			ui.click(new CTabItemLocator("Problems"));
			ui.click(auditRule);
			if (! expected && auditRule != null)
				fail("Rule with text "+message+"  is being fired unexpectedly");
		} catch (Exception noWidget){
			if ( expected)
				fail("Rule with text "+message+" is not being fired when expected");	
		}
	}
	
}
