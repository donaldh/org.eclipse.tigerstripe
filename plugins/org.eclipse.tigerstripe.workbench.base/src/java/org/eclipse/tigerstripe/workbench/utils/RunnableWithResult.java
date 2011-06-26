package org.eclipse.tigerstripe.workbench.utils;

public interface RunnableWithResult<T, E extends Throwable> {

	T run() throws E;

}
