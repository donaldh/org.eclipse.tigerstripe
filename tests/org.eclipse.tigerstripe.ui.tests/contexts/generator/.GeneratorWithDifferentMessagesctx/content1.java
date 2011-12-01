package com.test;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.plugins.PluginLog;

public class ContextEntry {
	private static final String PLUGIN_ID = "plugin.id";
	public String getValue() {
		  // ok
		  PluginLog.reportStatus(new Status(IStatus.OK, PLUGIN_ID, "message ok"));
		    
		  // info
		  PluginLog.reportStatus(new Status(IStatus.INFO, PLUGIN_ID, "message info"));

		  // warning
		  PluginLog
		    .reportStatus(new Status(IStatus.WARNING, PLUGIN_ID, "message warning"));

		  // error with stack trace
		  try {
		   Integer.parseInt("qwe");
		  } catch (Exception e) {
		   PluginLog.reportStatus(new Status(IStatus.ERROR, PLUGIN_ID,
		     "message error with stack trace", e));
		  }

		  // multi status
		  MultiStatus multiStatus = new MultiStatus(PLUGIN_ID, 222, "message",
		    null);
		  multiStatus.add(new Status(IStatus.OK, PLUGIN_ID, "message"));
		  multiStatus.add(new Status(IStatus.OK, PLUGIN_ID, "message"));
		  multiStatus.add(new Status(IStatus.OK, PLUGIN_ID, "message"));

		  PluginLog.reportStatus(multiStatus);

		  return null;
		 }

}
