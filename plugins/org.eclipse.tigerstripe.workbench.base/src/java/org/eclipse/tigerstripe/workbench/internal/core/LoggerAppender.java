package org.eclipse.tigerstripe.workbench.internal.core;


public interface LoggerAppender {

	void log(int level, String message, Throwable t);

}
