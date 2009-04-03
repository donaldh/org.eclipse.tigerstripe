/*******************************************************************************
 * Copyright (c) 2009 Cisco Systems, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    R. Craddock (Cisco Systems, Inc.)
 *******************************************************************************/
package org.eclipse.tigerstripe.generators.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import org.eclipse.tigerstripe.workbench.TigerstripeException;
import org.eclipse.tigerstripe.workbench.model.deprecated_.IAbstractArtifact;
import org.eclipse.tigerstripe.workbench.plugins.IExpander;
import org.eclipse.tigerstripe.workbench.plugins.IRule;
import org.eclipse.tigerstripe.workbench.plugins.IRuleReport;
import org.eclipse.tigerstripe.workbench.plugins.IRunnableWrapper;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;
import org.eclipse.tigerstripe.workbench.project.IPluginConfig;
import org.eclipse.tigerstripe.workbench.project.ITigerstripeModelProject;



public abstract class AbstractRunnable implements IRunnableWrapper{

	protected Map<String, Object> context ;

	protected IRuleReport report = null;
	protected IPluginConfig config = null;
	protected ITigerstripeModelProject modelProject = null;
	protected IExpander exp;
		

	public String makeFileName() throws TigerstripeException {
		String base = "";
		String target = "";
		String file = "";
		String dir = "";
		
		
		base = modelProject.getLocation().toString();
		target = modelProject.getProjectDetails().getOutputDirectory();
		
		String f = exp.expandVar(config.getProperty("fileName").toString());
		file = exp.expandVar(f);
		String d = exp.expandVar(config.getProperty("xmlDir").toString());
		dir = exp.expandVar(d);
		File dirs = new File(base+File.separator+
				  target+File.separator+
				  dir);
		dirs.mkdirs();
		
		String fullFileName = base+File.separator+
							  target+File.separator+
							  dir+File.separator+
							  file;
		return fullFileName;
	}
	
	public String makeSingleArtiFileName(IAbstractArtifact artifact) throws TigerstripeException{
		String base = "";
		String target = "";
		String file = "";
		String extension = ".xml";
		String path = "";
		String dir = "";
		
		base = modelProject.getLocation().toString();
		target = modelProject.getProjectDetails().getOutputDirectory();
		String d = exp.expandVar(config.getProperty("xmlDir").toString());
		dir = exp.expandVar(d);
		path = base+File.separator+target+File.separator+dir+File.separator+artifact.getPackage().replace(".", File.separator);
		File dirs = new File(path);
		dirs.mkdirs();
		
		String fullFileName = path+File.separator+artifact.getName()+extension;
		PluginLog.logDebug("This is the file name: "+ fullFileName);
		return fullFileName;
	}
	
	
	
	public void setContext(Map<String, Object> arg0) {
		this.context = arg0;
		PluginLog.logInfo("Setting context");
		
		Object rpt = context.get(IRule.REPORT);
		if (rpt instanceof IRuleReport){
			this.report = (IRuleReport) rpt;
			PluginLog.logTrace("Report : "+this.report);
		}
		
		Object pluginConfig = context.get(IRule.PLUGINCONFIG);
		if (pluginConfig instanceof IPluginConfig){
			
			config = (IPluginConfig) pluginConfig;
			PluginLog.logTrace("Got plugin Config");
			
		}
		Object proj = context.get(IRule.TSPROJECTHANDLE);
		if (proj instanceof ITigerstripeModelProject){
			modelProject = (ITigerstripeModelProject) proj;
			PluginLog.logTrace("Got model project");
		}
		
		exp = (IExpander) context.get(IRule.EXP);
	}

	public IRuleReport getReport() {
		return report;
	}
	
	public IPluginConfig getConfig() {
		return config;
	}
	
	/**
	 * Save the package to a Resource.
	 * 
	 * @param package_
	 * @param uri
	 */
	@SuppressWarnings("unchecked")	
       public void save(String fileName,String contents ) {
	    
	    File file = new File(fileName);
	    Object ovw = context.get(IRule.OVERWRITEFILES);
	    boolean isOverwriteFiles = false;
	    if (ovw instanceof Boolean){
	    	isOverwriteFiles = (Boolean) ovw;
	    	PluginLog.logDebug("IsOverwriteFiles "+Boolean.toString(isOverwriteFiles));
	    }

		try {
			PrintWriter out = new PrintWriter(new FileWriter(file));
			out.println(contents);
			out.close();
			PluginLog.logInfo("Export complete. "+file);
			getReport().getGeneratedFiles().add(file.toString());
		} catch (IOException ioe) {
			PluginLog.logError("Export failed to save. " + file, ioe);
		}
	}
	
}
