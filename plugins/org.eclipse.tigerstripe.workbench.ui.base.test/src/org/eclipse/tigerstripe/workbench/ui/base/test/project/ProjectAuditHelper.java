package org.eclipse.tigerstripe.workbench.ui.base.test.project;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class ProjectAuditHelper extends UITestCaseSWT {

	IUIContext ui;

	private String errors    = "Errors \\([0-9]* item[s]?\\)/";
	private String warnings  = "Warnings \\([0-9]* item[s]?\\)/";
	private String infos     = "Infos \\([0-9]* item[s]?\\)/";
	
	
	public ProjectAuditHelper(IUIContext ui) {
		super();
		this.ui = ui;
	}
	
	
	public void checkUndefinedProjectVersion(String project, boolean expected) throws Exception{
		checkRule(warnings, "Project "+project+" has no 'Project Version'", 
				  expected);		
	}
	
	public void checkUndefinedProjectDescription(String project, boolean expected) throws Exception{
		checkRule(infos, "Project "+project+" has no 'Project Description'", 
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
			ui.click(auditRule);
			if (! expected && auditRule != null)
				fail("Rule with text "+message+"  is being fired unexpectedly");
		} catch (Exception noWidget){
			if ( expected)
				fail("Rule with text "+message+" is not being fired when expected");	
		}
	}
	
}
