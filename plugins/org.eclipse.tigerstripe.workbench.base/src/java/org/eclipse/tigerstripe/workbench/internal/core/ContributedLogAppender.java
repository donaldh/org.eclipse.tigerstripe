package org.eclipse.tigerstripe.workbench.internal.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;

public class ContributedLogAppender implements LoggerAppender {

	private final List<LoggerAppender> delegats;

	private static List<LoggerAppender> fetch() {
		final IConfigurationElement[] elements = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						"org.eclipse.tigerstripe.workbench.base.runtimeLoggers");
		final List<LoggerAppender> appenders = new ArrayList<LoggerAppender>();
		for (final IConfigurationElement e : elements) {
			SafeRunner.run(new SafeRunnable() {
				public void run() throws Exception {
						appenders.add((LoggerAppender) e.createExecutableExtension("class"));
				}
			});
		}
		return appenders;
	}
	
	public ContributedLogAppender() {
		delegats = fetch();
	}
	
	public void log(int level, String message, Throwable t) {
		for (LoggerAppender loggerAppender : delegats) {
			loggerAppender.log(level, message, t);
		}
	}
}
