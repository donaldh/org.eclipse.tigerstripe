package org.eclipse.tigerstripe.workbench.ui.base.test.generator;

import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;
import com.windowtester.runtime.swt.locator.eclipse.ViewLocator;

public class RuleAuditHelper extends UITestCaseSWT {

	IUIContext ui;

	private String errors = "Errors \\([0-9]* item[s]?\\)/";
	private String infos  = "Infos \\([0-9]* item[s]?\\)/";
	
	
	public RuleAuditHelper(IUIContext ui) {
		super();
		this.ui = ui;
	}
	
	public void checkArtifactTypeUndefinedRule(String rule,String project,boolean expected) throws Exception{
		checkRule(errors, "Artifact Type undefined in [A-Za-z ]* '", 
				rule, project, expected);		
	}
	
	public void checkNoOutputFileRule(String rule,String project,boolean expected) throws Exception{
		checkRule(errors, "No specified output filename for [A-Za-z ]* '", 
				rule, project, expected);		
	}
	
	//No Template specified for Artifact Model Run Rule 'ArtifactRule' in project 'New Plugin Project'
	public void checkNoTemplateRule(String rule,String project,boolean expected) throws Exception{
		checkRule(errors, "No Template specified for [A-Za-z ]* '", 
				rule, project, expected);		
	}
	
	
	public void checkNoDescriptionRule(String rule,String project,boolean expected) throws Exception{
		checkRule(infos, "No description for [A-Za-z ]* '", 
				rule, project, expected);		
	}
	
	
	private void checkRule(
				String type, 
				String message,
				String rule,
				String project,
				boolean expected){
		TreeItemLocator auditRule = new TreeItemLocator(
				type+message+rule+"' in project '"+project+"'",
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
