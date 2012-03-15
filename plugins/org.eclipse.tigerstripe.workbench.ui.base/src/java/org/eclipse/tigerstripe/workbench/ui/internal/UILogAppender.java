package org.eclipse.tigerstripe.workbench.ui.internal;

import org.eclipse.core.runtime.Status;
import org.eclipse.tigerstripe.workbench.internal.BasePlugin;
import org.eclipse.tigerstripe.workbench.internal.core.LoggerAppender;

public class UILogAppender implements LoggerAppender {

	public void log(int level, String message, Throwable t) {
		BasePlugin.getDefault().getLog()
				.log(new Status(level, BasePlugin.PLUGIN_ID, message, t));
	}
}
